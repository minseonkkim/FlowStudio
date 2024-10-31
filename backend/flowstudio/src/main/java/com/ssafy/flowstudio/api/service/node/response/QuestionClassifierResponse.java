package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionClassifierResponse extends NodeResponse {

    private final String classList;

    @Builder
    private QuestionClassifierResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> sourceEdges, List<EdgeResponse> targetEdges, String classList) {
        super(nodeId, name, type, coordinate, sourceEdges, targetEdges);
        this.classList = classList;
    }

    public static QuestionClassifierResponse from(QuestionClassifier questionClassifier) {
        return QuestionClassifierResponse.builder()
                .nodeId(questionClassifier.getId())
                .name(questionClassifier.getName())
                .type(questionClassifier.getType())
                .coordinate(CoordinateResponse.from(questionClassifier.getCoordinate()))
                .sourceEdges(questionClassifier.getSourceEdges().stream().map(EdgeResponse::from).toList())
                .targetEdges(questionClassifier.getTargetEdges().stream().map(EdgeResponse::from).toList())
                .classList(questionClassifier.getClassList())
                .build();
    }

}
