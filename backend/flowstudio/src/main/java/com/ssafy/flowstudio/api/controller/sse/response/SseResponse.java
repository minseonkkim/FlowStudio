package com.ssafy.flowstudio.api.controller.sse.response;

import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SseResponse {

    private Long nodeId;
    private NodeType type;
    private String name;

    @Builder
    private SseResponse(Long nodeId, NodeType type, String name) {
        this.nodeId = nodeId;
        this.type = type;
        this.name = name;
    }

    public static SseResponse from(Node node) {
        return SseResponse.builder()
                .nodeId(node.getId())
                .type(node.getType())
                .name(node.getName())
                .build();
    }
}
