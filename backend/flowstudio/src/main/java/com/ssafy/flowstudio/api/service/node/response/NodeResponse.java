package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
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
    private List<EdgeResponse> sourceEdges;
    private List<EdgeResponse> targetEdges;

    protected NodeResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> sourceEdges, List<EdgeResponse> targetEdges) {
        this.nodeId = nodeId;
        this.name = name;
        this.type = type;
        this.coordinate = coordinate;
        this.sourceEdges = sourceEdges;
        this.targetEdges = targetEdges;
    }

//    public static NodeResponse from(Node node) {
//        return NodeResponse.builder()
//                .nodeId(node.getId())
//                .name(node.getName())
//                .type(node.getType())
//                .coordinate(CoordinateResponse.from(node.getCoordinate()))
//                .sourceEdges(node.getSourceEdges().stream().map(EdgeResponse::from).collect(Collectors.toList()))
//                .targetEdges(node.getTargetEdges().stream().map(EdgeResponse::from).collect(Collectors.toList()))
//                .build();
//    }

}
