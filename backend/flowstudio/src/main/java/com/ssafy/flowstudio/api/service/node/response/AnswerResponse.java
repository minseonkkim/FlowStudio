package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnswerResponse extends NodeResponse {

    private final String outputMessage;

    @Builder
    private AnswerResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> sourceEdges, List<EdgeResponse> targetEdges, String outputMessage) {
        super(nodeId, name, type, coordinate, sourceEdges, targetEdges);
        this.outputMessage = outputMessage;
    }

    public static AnswerResponse from(Answer answer) {
        return AnswerResponse.builder()
                .nodeId(answer.getId())
                .name(answer.getName())
                .type(answer.getType())
                .coordinate(CoordinateResponse.from(answer.getCoordinate()))
                .sourceEdges(answer.getSourceEdges().stream().map(EdgeResponse::from).toList())
                .targetEdges(answer.getTargetEdges().stream().map(EdgeResponse::from).toList())
                .outputMessage(answer.getOutputMessage())
                .build();
    }

}
