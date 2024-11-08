package com.ssafy.flowstudio.api.controller.edge.request;

import com.ssafy.flowstudio.api.service.edge.request.EdgeServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EdgeRequest {

    @NotNull(message = "sourceNodeId는 필수값입니다.")
    private Long sourceNodeId;
    @NotNull(message = "targetNodeId는 필수값입니다.")
    private Long targetNodeId;
    private Long sourceConditionId;

    @Builder
    private EdgeRequest(Long sourceNodeId, Long targetNodeId, Long sourceConditionId) {
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.sourceConditionId = sourceConditionId;
    }

    public EdgeServiceRequest toServiceRequest() {
        return EdgeServiceRequest.builder()
                .sourceNodeId(sourceNodeId)
                .targetNodeId(targetNodeId)
                .sourceConditionId(sourceConditionId)
                .build();
    }

}
