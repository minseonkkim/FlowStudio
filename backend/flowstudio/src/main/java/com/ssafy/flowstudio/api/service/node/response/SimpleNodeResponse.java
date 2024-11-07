package com.ssafy.flowstudio.api.service.node.response;


import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleNodeResponse {

    private Long nodeId;
    private String name;
    private NodeType type;

    @Builder
    private SimpleNodeResponse(Long nodeId, String name, NodeType type) {
        this.nodeId = nodeId;
        this.name = name;
        this.type = type;
    }

    public static SimpleNodeResponse from(Node node) {
        return SimpleNodeResponse.builder()
                .nodeId(node.getId())
                .name(node.getName())
                .type(node.getType())
                .build();
    }
}

