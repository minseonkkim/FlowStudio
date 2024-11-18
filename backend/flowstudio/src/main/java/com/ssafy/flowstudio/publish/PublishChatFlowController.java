package com.ssafy.flowstudio.publish;

import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.publish.response.PublishChatFlowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PublishChatFlowController {

    private final PublishService publishService;

    @GetMapping(value = "/api/v1/chat-flows/publish")
    public ApiResponse<List<ChatFlowListResponse>> getPublishChatFlow(
            @CurrentUser User user
    ) {
        return ApiResponse.ok(publishService.getPublishChatFlows(user));
    }

    @PostMapping(value = "/api/v1/chat-flows/{chatFlowId}/publish")
    public ApiResponse<PublishChatFlowResponse> publishChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        return ApiResponse.ok(publishService.publishChatFlow(user, chatFlowId));
    }

    @DeleteMapping(value = "/api/v1/chat-flows/{chatFlowId}/publish")
    public ApiResponse<Boolean> unPublishChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        return ApiResponse.ok(publishService.unPublishChatFlow(user, chatFlowId));
    }
}
