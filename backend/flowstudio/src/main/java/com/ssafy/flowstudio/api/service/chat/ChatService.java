package com.ssafy.flowstudio.api.service.chat;

import com.ssafy.flowstudio.api.service.chat.request.ChatCreateServiceRequest;
import com.ssafy.flowstudio.api.service.chat.request.ChatMessageServiceRequest;
import com.ssafy.flowstudio.api.service.chat.response.ChatCreateResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.NodeVisitor;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatService {

    private final NodeVisitor visitor;
    private final ChatRepository chatRepository;
    private final ChatFlowRepository chatFlowRepository;

    @Transactional
    public void sendMessage(Long chatId, ChatMessageServiceRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

        String message = request.getMessage();

        visitor.start(message, chat);
    }

    @Transactional
    public ChatCreateResponse createChat(User user, Long chatFlowId, ChatCreateServiceRequest request) {
        // 비로그인 유저면 익명 유저 객체 생성
        if (user == null) {
            user = User.createAnonymous();
        }
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        log.info("Preview: {}", request.isPreview());
        Chat chat = Chat.create(user, chatFlow, request.isPreview());
        Chat savedChat = chatRepository.save(chat);

        return ChatCreateResponse.from(savedChat);
    }

}
