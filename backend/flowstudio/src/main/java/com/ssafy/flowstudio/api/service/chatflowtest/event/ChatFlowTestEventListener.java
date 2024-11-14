package com.ssafy.flowstudio.api.service.chatflowtest.event;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestResponse;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.api.service.rag.request.LangchainClient;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflowtest.ChatFlowTestRepository;
import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTestCase;
import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatFlowTestEventListener {

    private final SseEmitters sseEmitters;
    private final RedisService redisService;
    private final LangchainClient langchainClient;

    private final ChatFlowTestCaseRepository chatFlowTestCaseRepository;

    @EventListener
    public void handleChatFlowTestEvent(ChatFlowTestEvent event) {
        Chat chat = event.getChat();
        ChatFlowTest chatFlowTest = chat.getChatFlowTest();

        String prediction = redisService.get("test:" + chat.getId());
        ChatFlowTestRequest chatFlowTestRequest = ChatFlowTestRequest.of(chat.getGroundTruth(), prediction);
        ChatFlowTestResponse chatFlowTestResponse = langchainClient.chatFlowTest(chatFlowTestRequest);

        // 받아온 결과 저장
        ChatFlowTestCase chatFlowTestCase = ChatFlowTestCase.create(
                chatFlowTest,
                chat.getTestQuestion(),
                chat.getGroundTruth(),
                prediction,
                chatFlowTestResponse.getEmbeddingDistance(),
                chatFlowTestResponse.getCrossEncoder(),
                chatFlowTestResponse.getRougeMetric()
        );
        chatFlowTestCaseRepository.save(chatFlowTestCase);

        // TODO: 모든 테스트케이스가 끝나면 총 수치 계산
        // 카운트를 1씩 늘려서 개수만큼 됐는지 확인 -> 챗플로우 테스트에 총 테케 개수 추가

        // 결과 전송
        sseEmitters.sendChatFlowTestResult(chat, chatFlowTestResponse);
    }

}
