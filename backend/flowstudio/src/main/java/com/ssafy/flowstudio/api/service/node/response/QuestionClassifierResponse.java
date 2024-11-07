package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QuestionClassifierResponse extends NodeResponse {

    private final String modelParamList;
    private final List<QuestionClassResponse> questionClasses;

    @Builder
    private QuestionClassifierResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, String modelParamList, List<QuestionClassResponse> questionClasses) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges);
        this.modelParamList = modelParamList;
        this.questionClasses = questionClasses;
    }

    public static QuestionClassifierResponse from(QuestionClassifier questionClassifier) {
        return QuestionClassifierResponse.builder()
                .nodeId(questionClassifier.getId())
                .name(questionClassifier.getName())
                .type(questionClassifier.getType())
                .coordinate(CoordinateResponse.from(questionClassifier.getCoordinate()))
                .outputEdges(questionClassifier.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(questionClassifier.getInputEdges().stream().map(EdgeResponse::from).toList())
                .modelParamList(questionClassifier.getModelParamList())
                .questionClasses(questionClassifier.getQuestionClasses().stream().map(QuestionClassResponse::from).toList())
                .build();
    }

}
