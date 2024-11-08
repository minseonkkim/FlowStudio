package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.node.response.*;
import com.ssafy.flowstudio.domain.node.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NodeDetailResponseMapper {

    public NodeResponse getCorrespondNodeDetailResponse(Node node, List<Node> precedingNodes) {
        return switch (node.getType()) {
            case START -> StartDetailResponse.of((Start) node, precedingNodes);
            case ANSWER -> AnswerDetailResponse.of((Answer) node, precedingNodes);
            case LLM -> LlmDetailResponse.of((LLM) node, precedingNodes);
            case QUESTION_CLASSIFIER -> QuestionClassifierDetailResponse.of((QuestionClassifier) node, precedingNodes);
            case RETRIEVER -> RetrieverDetailResponse.of((Retriever) node, precedingNodes);

            // DEPRECATED
            case CONDITIONAL -> ConditionalResponse.from((Conditional) node);
            case VARIABLE_ASSIGNER -> VariableAssignerResponse.from((VariableAssigner) node);
        };
    }

}