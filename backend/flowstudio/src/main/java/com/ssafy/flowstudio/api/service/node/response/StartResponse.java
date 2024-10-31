package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Start;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class StartResponse extends NodeResponse {

    private final int maxLength;

    @Builder
    public StartResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, int maxLength) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges);
        this.maxLength = maxLength;
    }

    public static StartResponse from(Start start) {
        return StartResponse.builder()
                .nodeId(start.getId())
                .name(start.getName())
                .type(start.getType())
                .coordinate(CoordinateResponse.from(start.getCoordinate()))
                .outputEdges(start.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(start.getInputEdges().stream().map(EdgeResponse::from).toList())
                .maxLength(start.getMaxLength())
                .build();
    }

}
