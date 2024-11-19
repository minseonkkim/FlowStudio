package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.Conditional;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ConditionalResponse extends NodeResponse {

    private final String subConditionalList;

    @Builder
    public ConditionalResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, String subConditionalList) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges);
        this.subConditionalList = subConditionalList;
    }

    public static ConditionalResponse from(Conditional conditional) {
        return ConditionalResponse.builder()
                .nodeId(conditional.getId())
                .name(conditional.getName())
                .type(conditional.getType())
                .coordinate(CoordinateResponse.from(conditional.getCoordinate()))
                .outputEdges(conditional.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(conditional.getInputEdges().stream().map(EdgeResponse::from).toList())
                .subConditionalList(conditional.getSubConditionalList())
                .build();
    }

}
