package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.domain.edge.entity.Edge;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EdgeResponse {

    private final Long edgeId;
    private final Long sourceNodeId;
    private final Long targetNodeId;
    private final Long sourceConditionId;

    @Builder
    private EdgeResponse(Long edgeId, Long sourceNodeId, Long targetNodeId, Long sourceConditionId) {
        this.edgeId = edgeId;
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.sourceConditionId = sourceConditionId;
    }

    public static EdgeResponse from(Edge edge) {
        return EdgeResponse.builder()
                .edgeId(edge.getId())
                .sourceNodeId(edge.getSourceNode().getId())
                .targetNodeId(edge.getTargetNode().getId())
                .build();
    }

}
