package com.ssafy.flowstudio.api.controller.sse.response;

import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SseNodeResponse {

    private Long nodeId;
    private NodeType type;
    private String name;
    private String message;

    @Builder
    private SseNodeResponse(Long nodeId, NodeType type, String name, String message) {
        this.nodeId = nodeId;
        this.type = type;
        this.name = name;
        this.message = message;
    }

    public static SseNodeResponse from(Node node) {
        return SseNodeResponse.builder()
                .nodeId(node.getId())
                .type(node.getType())
                .name(node.getName())
                .build();
    }

    public static SseNodeResponse of(Node node, String message) {
        return SseNodeResponse.builder()
                .nodeId(node.getId())
                .type(node.getType())
                .name(node.getName())
                .message(message)
                .build();
    }
}
