package com.ssafy.flowstudio.api.controller.chatflowtest;

import com.ssafy.flowstudio.api.controller.chatflowtest.request.ChatFlowTestRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.ChatFlowTestCreateResponse;
import com.ssafy.flowstudio.api.service.chatflowtest.ChatFlowTestService;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestDetailResponse;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestListResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatFlowTestController {

    private final ChatFlowTestService chatFlowTestService;

    /**
     * 챗플로우 테스트 목록 조회
     *
     * @param user
     * @param chatFlowId
     * @return
     */
    @GetMapping(value = "/api/v1/chat-flows/{chatFlowId}/tests")
    public ApiResponse<List<ChatFlowTestListResponse>> getChatFlowTests(
            @CurrentUser User user,
            @PathVariable Long chatFlowId
    ) {
        return ApiResponse.ok(chatFlowTestService.getChatFlowTests(user, chatFlowId));
    }

    /**
     * 챗플로우 테스트 상세 조회
     * @param user
     * @return
     */
    @GetMapping(value = "/api/v1/chat-flows/{chatFlowId}/tests/{chatFlowTestId}")
    public ApiResponse<ChatFlowTestDetailResponse> getChatFlowTest(
            @CurrentUser User user,
            @PathVariable Long chatFlowTestId
    ) {
        return ApiResponse.ok(chatFlowTestService.getChatFlowTest(user, chatFlowTestId));
    }

    /**
     * 챗플로우 테스트 생성
     *
     * @param user
     * @param chatFlowId
     * @param request
     * @return
     */
    @PostMapping(value = "/api/v1/chat-flows/{chatFlowId}/tests")
    public ApiResponse<List<ChatFlowTestCreateResponse>> createChatFlowTest(
            @CurrentUser User user,
            @PathVariable Long chatFlowId,
            @Valid @RequestBody List<ChatFlowTestRequest> request
    ) {
        return ApiResponse.ok(chatFlowTestService.createChatFlowTest(user, chatFlowId, request.stream().map(ChatFlowTestRequest::toServiceRequest).toList()));
    }

}
