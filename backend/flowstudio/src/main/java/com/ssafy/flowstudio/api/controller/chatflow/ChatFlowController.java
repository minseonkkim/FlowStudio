package com.ssafy.flowstudio.api.controller.chatflow;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowCreateRequest;
import com.ssafy.flowstudio.api.service.chatflow.ChatFlowService;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowUpdateResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatFlowController {

    private final ChatFlowService chatFlowService;

    @GetMapping(value = "/api/v1/chat-flows")
    public ApiResponse<List<ChatFlowListResponse>> getChatFlows(
            @CurrentUser User user
    ) {
        return ApiResponse.ok(chatFlowService.getChatFlows(user));
    }

    @GetMapping(value = "/api/v1/chat-flows/{chatFlowId}")
    public ApiResponse<ChatFlowResponse> getChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        return ApiResponse.ok(chatFlowService.getChatFlow(user, chatFlowId));
    }

    @PostMapping(value = "/api/v1/chat-flows")
    public ApiResponse<ChatFlowResponse> createChatFlow(
            @CurrentUser User user,
            @Valid @RequestBody ChatFlowCreateRequest request
    ) {
        return ApiResponse.ok(chatFlowService.createChatFlow(user, request.toServiceRequest()));
    }

    @PatchMapping(value = "/api/v1/chat-flows/{chatFlowId}")
    public ApiResponse<ChatFlowUpdateResponse> updateChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId,
            @Valid @RequestBody ChatFlowCreateRequest request
    ) {
        return ApiResponse.ok(chatFlowService.updateChatFlow(user, chatFlowId, request.toServiceRequest()));
    }

    @DeleteMapping(value = "/api/v1/chat-flows/{chatFlowId}")
    public ApiResponse<Void> deleteChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        chatFlowService.deleteChatFlow(user, chatFlowId);

        return ApiResponse.ok();
    }
}
