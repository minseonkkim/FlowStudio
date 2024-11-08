package com.ssafy.flowstudio.api.service.chat;

import com.ssafy.flowstudio.api.service.chat.request.ChatMessageServiceRequest;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
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

    @Transactional
    public void chat(User user, Long chatId, ChatMessageServiceRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

        String message = request.getMessage();

        visitor.start(message, chat);
    }

}
