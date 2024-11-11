package com.ssafy.flowstudio.api.controller.chatflow;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowRequest;
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

    /**
     * 챗플로우 목록 조회
     * @param user
     * @return List<ChatFlowListResponse>
     */
    @GetMapping(value = "/api/v1/chat-flows")
    public ApiResponse<List<ChatFlowListResponse>> getChatFlows(
            @CurrentUser User user
    ) {
        return ApiResponse.ok(chatFlowService.getChatFlows(user));
    }

    /**
     * 챗플로우 조회
     * @param user
     * @param chatFlowId
     * @return ChatFlowResponse
     */
    @GetMapping(value = "/api/v1/chat-flows/{chatFlowId}")
    public ApiResponse<ChatFlowResponse> getChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        return ApiResponse.ok(chatFlowService.getChatFlow(user, chatFlowId));
    }

    /**
     * 챗플로우 생성
     * @param user
     * @param request
     * @return ChatFlowResponse
     */
    @PostMapping(value = "/api/v1/chat-flows")
    public ApiResponse<ChatFlowResponse> createChatFlow(
            @CurrentUser User user,
            @Valid @RequestBody ChatFlowRequest request
    ) {
        return ApiResponse.ok(chatFlowService.createChatFlow(user, request.toServiceRequest()));
    }

    /**
     * 챗플로우 수정
     * @param user
     * @param chatFlowId
     * @param request
     * @return ChatFlowUpdateResponse
     */
    @PatchMapping(value = "/api/v1/chat-flows/{chatFlowId}")
    public ApiResponse<ChatFlowUpdateResponse> updateChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId,
            @Valid @RequestBody ChatFlowRequest request
    ) {
        return ApiResponse.ok(chatFlowService.updateChatFlow(user, chatFlowId, request.toServiceRequest()));
    }

    /**
     * 챗플로우 삭제
     * @param user
     * @param chatFlowId
     * @return ApiResponse<Void>
     */
    @DeleteMapping(value = "/api/v1/chat-flows/{chatFlowId}")
    public ApiResponse<Void> deleteChatFlow(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        chatFlowService.deleteChatFlow(user, chatFlowId);

        return ApiResponse.ok();
    }

    /**
     * 챗플로우 예제 생성
     * @param user
     * @return
     */
    @PostMapping("/api/v1/chat-flows/example")
    public ApiResponse<ChatFlowResponse> createExampleChatFlow(
            @CurrentUser User user
    ) {
        return ApiResponse.ok(chatFlowService.createExampleChatFlow(user));
    }

}
