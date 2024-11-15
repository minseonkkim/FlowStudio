package com.ssafy.flowstudio.api.service.chatflowtest;

import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestServiceRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestDetailResponse;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestListResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.chatflowtest.ChatFlowTestRepository;
import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatFlowTestService {

    private final ChatRepository chatRepository;
    private final ChatFlowRepository chatFlowRepository;
    private final ChatFlowTestRepository chatFlowTestRepository;
    private final TestExecutor testExecutor;

    private final EntityManager em;

    public List<ChatFlowTestListResponse> getChatFlowTests(User user, Long chatFlowId) {
        ChatFlow chatFlow = chatFlowRepository.findByIdWithTests(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().getId().equals(user.getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        return chatFlow.getTests().stream()
                .map(ChatFlowTestListResponse::from)
                .toList();
    }

    public ChatFlowTestDetailResponse getChatFlowTest(User user, Long chatFlowTestId) {
        ChatFlowTest chatFlowTest = chatFlowTestRepository.findByIdWithTestCase(chatFlowTestId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_TEST_NOT_FOUND));

        if (!chatFlowTest.getUser().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        return ChatFlowTestDetailResponse.from(chatFlowTest);
    }

    @Transactional
    public List<Long> createChatFlowTest(User user, Long chatFlowId, List<ChatFlowTestServiceRequest> request) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        ChatFlowTest chatFlowTest = ChatFlowTest.create(user, chatFlow, request.size());
        chatFlowTestRepository.save(chatFlowTest);

        List<Long> chatIds = new ArrayList<>();

        for (ChatFlowTestServiceRequest chatFlowTestServiceRequest : request) {
            String testQuestion = chatFlowTestServiceRequest.getTestQuestion();
            String groundTruth = chatFlowTestServiceRequest.getGroundTruth();

            Chat chat = Chat.createTestChat(user, chatFlow, true, chatFlowTest, testQuestion, groundTruth);
            chatRepository.save(chat);
            em.flush();

            chatIds.add(chat.getId());

            testExecutor.execute(chat.getId(), chatFlowTestServiceRequest);
        }

        return chatIds;
    }

}
