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

    private final String sub_conditional_list;

    @Builder
    private ConditionalResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> sourceEdges, List<EdgeResponse> targetEdges, String subConditionalList) {
        super(nodeId, name, type, coordinate, sourceEdges, targetEdges);
        sub_conditional_list = subConditionalList;
    }

    public static ConditionalResponse from(Conditional conditional) {
        return ConditionalResponse.builder()
                .nodeId(conditional.getId())
                .name(conditional.getName())
                .type(conditional.getType())
                .coordinate(CoordinateResponse.from(conditional.getCoordinate()))
                .sourceEdges(conditional.getSourceEdges().stream().map(EdgeResponse::from).toList())
                .targetEdges(conditional.getTargetEdges().stream().map(EdgeResponse::from).toList())
                .subConditionalList(conditional.getSubConditionalList())
                .build();
    }

}
