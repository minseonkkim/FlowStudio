package com.ssafy.flowstudio.api.controller.node;

import com.azure.core.annotation.Put;
import com.ssafy.flowstudio.api.controller.node.request.QuestionClassCreateRequest;
import com.ssafy.flowstudio.api.controller.node.request.QuestionClassUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.response.QuestionClassResponse;
import com.ssafy.flowstudio.api.service.node.QuestionClassService;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class QuestionClassController {

    private final QuestionClassService questionClassService;

    /**
     * 질문 분류 생성
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

    @PutMapping("/api/v1/chat-flows/nodes/question-classes/{questionClassId}")
    public ApiResponse<QuestionClassResponse> updateQuestionClass(
            @CurrentUser User user,
            @PathVariable Long questionClassId,
            @Valid @RequestBody QuestionClassUpdateRequest request
    ) {
        QuestionClassResponse questionClassResponse = questionClassService.updateQuestionClass(questionClassId, request.toServiceRequest());
        return ApiResponse.ok(questionClassResponse);
    }
}
