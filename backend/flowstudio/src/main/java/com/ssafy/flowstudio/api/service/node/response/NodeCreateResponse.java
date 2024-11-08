package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NodeCreateResponse {
    private final Long nodeId;
    private final NodeType nodeType;

    @Builder
    private NodeCreateResponse(Long nodeId, NodeType nodeType) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
    }

    public static NodeCreateResponse from(Node node) {
        return NodeCreateResponse.builder()
                .nodeId(node.getId())
                .nodeType(node.getType())
                .build();
    }

}
