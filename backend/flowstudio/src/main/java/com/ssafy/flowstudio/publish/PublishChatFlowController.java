package com.ssafy.flowstudio.publish;

import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PublishChatFlowController {

    private final PublishService publishService;

    @GetMapping(value = "/api/v1/publish/{chatFlowId}")
    public ApiResponse<ChatFlowResponse> getPublishChatFlow(
            @PathVariable Long chatFlowId
    ) {
        return ApiResponse.ok(publishService.getPublishChatFlow(chatFlowId));
    }

    @PostMapping(value = "/api/v1/publish/{chatFlowId}")
    public ApiResponse<String> publishChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        String publishUrl = publishService.publishChatFlow(chatFlowId);
        return ApiResponse.ok(publishUrl);
    }

    @GetMapping(value = "/api/v1/chats/{publishUrl}")
    public ApiResponse<Long> sendChatMessage(
            @PathVariable String publishUrl
    ) {
        return ApiResponse.ok(publishService.getChatId(publishUrl));
    }
}
