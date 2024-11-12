package com.ssafy.flowstudio.api.controller.edge;

import com.ssafy.flowstudio.api.controller.edge.request.EdgeRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.edge.EdgeService;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class EdgeController {

    private final EdgeService edgeService;

    @PostMapping(value = "/api/v1/chat-flows/{chatFlowId}/edges")
    public ApiResponse<EdgeResponse> createEdge(
            @CurrentUser User user,
            @PathVariable Long chatFlowId,
            @Valid @RequestBody EdgeRequest request
    ) {
        return ApiResponse.ok(edgeService.create(user, chatFlowId, request.toServiceRequest()));
    }

    @PutMapping(value = "/api/v1/chat-flows/{chatFlowId}/edges/{edgeId}")
    public ApiResponse<EdgeResponse> updateEdge(
            @CurrentUser User user,
            @PathVariable Long chatFlowId,
            @PathVariable Long edgeId,
            @Valid @RequestBody EdgeRequest request
    ) {
        return ApiResponse.ok(edgeService.update(user, chatFlowId, edgeId, request.toServiceRequest()));
    }

    @DeleteMapping(value = "/api/v1/chat-flows/{chatFlowId}/edges/{edgeId}")
    public ApiResponse<Boolean> deleteEdge(
            @CurrentUser User user,
            @PathVariable Long chatFlowId,
            @PathVariable Long edgeId
    ) {
        edgeService.delete(user, chatFlowId, edgeId);
        return ApiResponse.ok();
    }

}
