package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.response.AnswerResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnswerDetailResponse extends NodeResponse {

    private final String outputMessage;

    @Builder
    public AnswerDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, String outputMessage, List<SimpleNodeResponse> precedingNodes) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges, precedingNodes);
        this.outputMessage = outputMessage;
    }

    public static AnswerDetailResponse of(Answer answer, List<Node> precedingNodes) {
        return AnswerDetailResponse.builder()
                .nodeId(answer.getId())
                .name(answer.getName())
                .type(answer.getType())
                .coordinate(CoordinateResponse.from(answer.getCoordinate()))
                .outputEdges(answer.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(answer.getInputEdges().stream().map(EdgeResponse::from).toList())
                .outputMessage(answer.getOutputMessage())
                .precedingNodes(precedingNodes.stream().map(SimpleNodeResponse::from).toList())
                .build();
    }
}
