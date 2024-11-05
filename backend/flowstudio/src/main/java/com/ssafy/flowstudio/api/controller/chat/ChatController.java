package com.ssafy.flowstudio.api.controller.chat;

import com.ssafy.flowstudio.api.controller.chat.request.ChatMessageRequest;
import com.ssafy.flowstudio.api.service.chat.ChatService;
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

    @PostMapping(value = "api/v1/chats/{chatId}")
    public ApiResponse<Void> createChatMessage(
            @CurrentUser User user,
            @PathVariable Long chatId,
            @Valid @RequestBody ChatMessageRequest request
    ) {
        chatService.chat(user, chatId, request.toServiceRequest());
        return ApiResponse.ok();
    }
}
