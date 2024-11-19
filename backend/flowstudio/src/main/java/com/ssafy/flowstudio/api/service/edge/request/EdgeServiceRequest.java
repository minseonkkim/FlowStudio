package com.ssafy.flowstudio.api.service.edge.request;

import lombok.Builder;
import lombok.Getter;


@Getter
public class EdgeServiceRequest {

    private final Long sourceNodeId;
    private final Long targetNodeId;
    private final Long sourceConditionId;

    @Builder
    private EdgeServiceRequest(Long sourceNodeId, Long targetNodeId, Long sourceConditionId) {
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.sourceConditionId = sourceConditionId;
    }

}
