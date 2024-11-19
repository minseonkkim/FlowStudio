package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Start;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class StartDetailResponse extends NodeDetailResponse {

    private final int maxLength;

    @Builder
    public StartDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, List<SimpleNodeResponse> precedingNodes, int maxLength) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges, precedingNodes);
        this.maxLength = maxLength;
    }

    public static StartDetailResponse of(Start start, List<Node> precedingNodes) {
        return StartDetailResponse.builder()
                .nodeId(start.getId())
                .name(start.getName())
                .type(start.getType())
                .coordinate(CoordinateResponse.from(start.getCoordinate()))
                .outputEdges(start.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(start.getInputEdges().stream().map(EdgeResponse::from).toList())
                .precedingNodes(precedingNodes.stream().map(SimpleNodeResponse::from).toList())
                .maxLength(start.getMaxLength())
                .build();
    }
}
