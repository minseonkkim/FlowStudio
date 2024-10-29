package com.ssafy.flowstudio.api.service.node.request;

import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NodeCreateServiceRequest {

    private final Long chatFlowId;
    private final CoordinateServiceRequest coordinate;
    private final NodeType type;

    @Builder
    private NodeCreateServiceRequest(Long chatFlowId, CoordinateServiceRequest coordinate, NodeType type) {
        this.chatFlowId = chatFlowId;
        this.coordinate = coordinate;
        this.type = type;
    }

}
