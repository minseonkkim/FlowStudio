package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NodeDetailResponse {

    private Long nodeId;
    private String name;
    private NodeType type;
    private CoordinateResponse coordinate;
    private List<EdgeResponse> outputEdges;
    private List<EdgeResponse> inputEdges;
    private List<SimpleNodeResponse> precedingNodes;

    protected NodeDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, List<SimpleNodeResponse> precedingNodes) {
        this.nodeId = nodeId;
        this.name = name;
        this.type = type;
        this.coordinate = coordinate;
        this.outputEdges = outputEdges;
        this.inputEdges = inputEdges;
        this.precedingNodes = precedingNodes;
    }
}
