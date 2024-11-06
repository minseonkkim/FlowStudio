package com.ssafy.flowstudio.api.controller.node;

import com.ssafy.flowstudio.api.controller.node.request.QuestionClassCreateRequest;
import com.ssafy.flowstudio.api.controller.node.response.QuestionClassResponse;
import com.ssafy.flowstudio.api.service.node.QuestionClassService;
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
public class QuestionClassController {

    private final QuestionClassService questionClassService;

    /**
     * 질문 분류 생성
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/api/v1/chat-flows/nodes/{nodeId}/question-classes")
    public ApiResponse<QuestionClassResponse> createQuestionClass(
            @CurrentUser User user,
            @PathVariable Long nodeId,
            @Valid @RequestBody QuestionClassCreateRequest request
    ) {
        QuestionClassResponse questionClassResponse = questionClassService.createQuestionClass(nodeId, request.toServiceRequest());
        return ApiResponse.ok(questionClassResponse);
    }
}
