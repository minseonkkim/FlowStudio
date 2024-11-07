package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NodeResponse {

    private Long nodeId;
    private String name;
    private NodeType type;
    private CoordinateResponse coordinate;
    private List<EdgeResponse> outputEdges;
    private List<EdgeResponse> inputEdges;
    private List<SimpleNodeResponse> precedingNodes;

    protected NodeResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges) {
        this.nodeId = nodeId;
        this.name = name;
        this.type = type;
        this.coordinate = coordinate;
        this.outputEdges = outputEdges;
        this.inputEdges = inputEdges;
    }

    public void updatePrecedingNodes(List<Node> precedingNodes) {
        this.precedingNodes = precedingNodes.stream().map(SimpleNodeResponse::from).toList();
    }
}
