package com.ssafy.flowstudio.api.controller.node;

import com.ssafy.flowstudio.api.controller.node.request.QuestionClassUpdateRequest;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassResponse;
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
            @PathVariable Long nodeId
    ) {
        QuestionClassResponse questionClassResponse = questionClassService.createQuestionClass(nodeId);
        return ApiResponse.ok(questionClassResponse);
    }

    /**
     * 질문 분류 수정
     */
    @PutMapping("/api/v1/chat-flows/nodes/question-classes/{questionClassId}")
    public ApiResponse<QuestionClassResponse> updateQuestionClass(
            @CurrentUser User user,
            @PathVariable Long questionClassId,
            @Valid @RequestBody QuestionClassUpdateRequest request
    ) {
        QuestionClassResponse questionClassResponse = questionClassService.updateQuestionClass(questionClassId, request.toServiceRequest());
        return ApiResponse.ok(questionClassResponse);
    }
    
    /**
     * 질문 분류 삭제
     */
    @DeleteMapping("/api/v1/chat-flows/nodes/question-classes/{questionClassId}")
    public ApiResponse<Boolean> deleteQuestionClass(
            @CurrentUser User user,
            @PathVariable Long questionClassId
    ) {
        return ApiResponse.ok(questionClassService.deleteQuestionClass(questionClassId));
    }

}
