package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.VariableAssigner;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class VariableAssignerResponse extends NodeResponse {



    @Builder
    private VariableAssignerResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> sourceEdges, List<EdgeResponse> targetEdges) {
        super(nodeId, name, type, coordinate, sourceEdges, targetEdges);
    }

    public static VariableAssignerResponse from(VariableAssigner variableAssigner) {
        return VariableAssignerResponse.builder()
                .nodeId(variableAssigner.getId())
                .name(variableAssigner.getName())
                .type(variableAssigner.getType())
                .coordinate(CoordinateResponse.from(variableAssigner.getCoordinate()))
                .sourceEdges(variableAssigner.getSourceEdges().stream().map(EdgeResponse::from).toList())
                .targetEdges(variableAssigner.getTargetEdges().stream().map(EdgeResponse::from).toList())

                .build();
    }

}
