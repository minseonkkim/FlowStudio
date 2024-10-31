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
    public AnswerResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, String outputMessage) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges);
        this.outputMessage = outputMessage;
    }

    public static AnswerResponse from(Answer answer) {
        return AnswerResponse.builder()
                .nodeId(answer.getId())
                .name(answer.getName())
                .type(answer.getType())
                .coordinate(CoordinateResponse.from(answer.getCoordinate()))
                .outputEdges(answer.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(answer.getInputEdges().stream().map(EdgeResponse::from).toList())
                .outputMessage(answer.getOutputMessage())
                .build();
    }

}
