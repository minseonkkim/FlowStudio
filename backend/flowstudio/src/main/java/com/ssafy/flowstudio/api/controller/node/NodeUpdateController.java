package com.ssafy.flowstudio.api.controller.node;

import com.ssafy.flowstudio.api.controller.node.request.update.AnswerUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.LlmUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.QuestionClassifierUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.RetrieverUpdateRequest;
import com.ssafy.flowstudio.api.controller.node.request.update.StartUpdateRequest;
import com.ssafy.flowstudio.api.service.node.NodeUpdateService;
import com.ssafy.flowstudio.api.service.node.response.AnswerResponse;
import com.ssafy.flowstudio.api.service.node.response.LlmResponse;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassifierResponse;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.api.service.node.response.StartResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.LlmDetailResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NodeUpdateController {

    private final NodeUpdateService nodeUpdateService;

    /**
     * 시작 노드 업데이트
     * @param user
     * @param request
     * @return
     */
    @PutMapping(value = "/api/v1/chat-flows/nodes/{nodeId}/start")
    public ApiResponse<StartResponse> updateStart(
            @CurrentUser User user,
            @PathVariable Long nodeId,
            @Valid @RequestBody StartUpdateRequest request
    ) {
        return ApiResponse.ok(nodeUpdateService.updateStart(user, nodeId, request.toServiceRequest()));
    }

    /**
     * 질문분류기 노드 업데이트
     * @param user
     * @param request
     * @return
     */
    @PutMapping(value = "/api/v1/chat-flows/nodes/{nodeId}/question-classifier")
    public ApiResponse<QuestionClassifierResponse> updateQuestionClassifier(
            @CurrentUser User user,
            @PathVariable Long nodeId,
            @Valid @RequestBody QuestionClassifierUpdateRequest request
    ) {
        return ApiResponse.ok(nodeUpdateService.updateQuestionClassifier(user, nodeId, request.toServiceRequest()));
    }

    /**
     * LLM 노드 업데이트
     * @param user
     * @param request
     * @return
     */
    @PutMapping(value = "/api/v1/chat-flows/nodes/{nodeId}/llm")
    public ApiResponse<LlmDetailResponse> updateLlm(
            @CurrentUser User user,
            @PathVariable Long nodeId,
            @Valid @RequestBody LlmUpdateRequest request
    ) {
        return ApiResponse.ok(nodeUpdateService.updateLlm(user, nodeId, request.toServiceRequest()));
    }

    /**
     * 지식검색기 노드 업데이트
     * @param user
     * @param request
     * @return
     */
    @PutMapping(value = "/api/v1/chat-flows/nodes/{nodeId}/retriever")
    public ApiResponse<RetrieverResponse> updateRetriever(
            @CurrentUser User user,
            @PathVariable Long nodeId,
            @Valid @RequestBody RetrieverUpdateRequest request
    ) {
        return ApiResponse.ok(nodeUpdateService.updateRetriever(user, nodeId, request.toServiceRequest()));
    }

    /**
     * 답변 노드 업데이트
     * @param user
     * @param request
     * @return
     */
    @PutMapping(value = "/api/v1/chat-flows/nodes/{nodeId}/answer")
    public ApiResponse<AnswerResponse> updateAnswer(
            @CurrentUser User user,
            @PathVariable Long nodeId,
            @Valid @RequestBody AnswerUpdateRequest request
    ) {
        return ApiResponse.ok(nodeUpdateService.updateAnswer(user, nodeId, request.toServiceRequest()));
    }

}
