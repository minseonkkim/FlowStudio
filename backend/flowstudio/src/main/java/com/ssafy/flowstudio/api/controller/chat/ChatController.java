package com.ssafy.flowstudio.api.controller.chat;

import com.ssafy.flowstudio.api.controller.chat.request.ChatCreateRequest;
import com.ssafy.flowstudio.api.controller.chat.request.ChatMessageRequest;
import com.ssafy.flowstudio.api.service.chat.ChatService;
import com.ssafy.flowstudio.api.service.chat.response.ChatCreateResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;

    /**
     * 채팅 생성
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/api/v1/chat-flows/{chatFlowId}/chats")
    public ApiResponse<ChatCreateResponse> createChat(
            @CurrentUser User user,
            @PathVariable Long chatFlowId,
            @Valid @RequestBody ChatCreateRequest request
    ) {
        return ApiResponse.ok(chatService.createChat(user, chatFlowId, request.toServiceRequest()));
    }

    /**
     * 채팅 메시지 생성
     * @param chatId
     * @param request
     * @return
     */
    @PostMapping(value = "/api/v1/chats/{chatId}")
    public ApiResponse<Void> sendChatMessage(
            @PathVariable Long chatId,
            @Valid @RequestBody ChatMessageRequest request
    ) {
        chatService.sendMessage(chatId, request.toServiceRequest());
        return ApiResponse.ok();
    }

}
