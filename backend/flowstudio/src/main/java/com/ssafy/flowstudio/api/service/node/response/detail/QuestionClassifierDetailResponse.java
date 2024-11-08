package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassResponse;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassifierResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionClassifierDetailResponse extends NodeResponse {

    private final List<QuestionClassResponse> questionClasses;

    @Builder
    private QuestionClassifierDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, List<QuestionClassResponse> questionClasses, List<SimpleNodeResponse> precedingNodes) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges, precedingNodes);
        this.questionClasses = questionClasses;
    }

    public static QuestionClassifierDetailResponse of(QuestionClassifier questionClassifier, List<Node> precedingNodes) {
        return QuestionClassifierDetailResponse.builder()
                .nodeId(questionClassifier.getId())
                .name(questionClassifier.getName())
                .type(questionClassifier.getType())
                .coordinate(CoordinateResponse.from(questionClassifier.getCoordinate()))
                .outputEdges(questionClassifier.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(questionClassifier.getInputEdges().stream().map(EdgeResponse::from).toList())
                .precedingNodes(precedingNodes.stream().map(SimpleNodeResponse::from).toList())
                .questionClasses(questionClassifier.getQuestionClasses().stream().map(QuestionClassResponse::from).toList())
                .build();
    }
}
