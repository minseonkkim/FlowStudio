package com.ssafy.flowstudio.api.service.chat;

import com.ssafy.flowstudio.api.controller.request.ChatMessageRequest;
import com.ssafy.flowstudio.api.service.chat.request.ChatMessageServiceRequest;
import com.ssafy.flowstudio.api.service.node.executor.NodeExecutor;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeVisitor;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatService {

    private final NodeVisitor visitor;
    private final ChatRepository chatRepository;

    @Transactional
    public void chat(User user, Long chatId, ChatMessageServiceRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

        String message = request.getMessage();

        visitor.start(message, chat);
    }

}
