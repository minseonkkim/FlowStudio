package com.ssafy.flowstudio.api.service.chat;

import com.ssafy.flowstudio.api.service.chat.request.ChatCreateServiceRequest;
import com.ssafy.flowstudio.api.service.chat.request.ChatMessageServiceRequest;
import com.ssafy.flowstudio.api.service.chat.response.ChatCreateResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatDetailResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatListResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.security.jwt.JWTService;
import com.ssafy.flowstudio.common.security.jwt.JwtToken;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.NodeVisitor;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.publish.PublishService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatService {

    private final NodeVisitor visitor;
    private final ChatRepository chatRepository;
    private final ChatFlowRepository chatFlowRepository;
    private final UserRepository userRepository;

    private final JWTService jwtService;
    private final PublishService publishService;

    @Transactional
    public void sendMessage(Long chatId, ChatMessageServiceRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

        if (!chat.isPreview()) {
            ChatFlow publishChatFlow = publishService.getPublishChatFlow(chat.getChatFlow().getPublishUrl());
            chat.updateChatFlow(publishChatFlow);
        }

        String message = request.getMessage();

        visitor.start(message, chat);
    }

    @Transactional
    public ChatCreateResponse createChat(User user, Long chatFlowId, ChatCreateServiceRequest request, HttpServletResponse servletResponse) {
        if (user == null) {
            String username = "anonymous " + UUID.randomUUID().toString().substring(0, 10);
            user = User.createAnonymous(username);
            userRepository.save(user);
            Collection<GrantedAuthority> collection = new ArrayList<>();
            collection.add((GrantedAuthority) () -> "ROLE_ANONYMOUS");

            JwtToken token = jwtService.generateToken(user.getUsername(), collection);
            servletResponse.setHeader("Authorization", token.getAccessToken());
        }

        ChatFlow chatFlow;
        if (!request.isPreview()) {
            chatFlow = publishService.getPublishChatFlow(chatFlowId);
        } else {
            chatFlow = chatFlowRepository.findById(chatFlowId)
                    .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));
        }

        log.info("Preview: {}", request.isPreview());
        Chat chat = Chat.create(user, chatFlow, request.isPreview());
        Chat savedChat = chatRepository.save(chat);

        return ChatCreateResponse.from(savedChat);
    }

    public ChatListResponse getChats(User user, Long chatFlowId, int page, int limit) {
        PageRequest pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        List<Chat> chats = chatRepository.findByChatFlowAndUser(chatFlow, user, pageable);
        int totalCount = chatRepository.findChatCountByChatFlowAndUser(chatFlow, user);

        return ChatListResponse.of(chatFlow, chats, totalCount);
    }

    public ChatDetailResponse getChat(User user, Long chatFlowId, Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

        if (!chat.getUser().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        return ChatDetailResponse.from(chat);
    }

    @Transactional
    public boolean deleteChat(User user, Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

        if (!chat.getUser().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        chatRepository.delete(chat);
        return true;
    }

}
