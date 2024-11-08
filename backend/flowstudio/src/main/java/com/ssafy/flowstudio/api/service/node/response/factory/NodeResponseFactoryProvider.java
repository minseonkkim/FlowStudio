package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.domain.node.entity.NodeType;

public class NodeResponseFactoryProvider {

    public static NodeResponseFactory getFactory(NodeType type) {
        return switch (type) {
            case START -> new StartResponseFactory();
            case ANSWER -> new AnswerResponseFactory();
            case LLM -> new LlmResponseFactory();
            case QUESTION_CLASSIFIER -> new QuestionClassifierResponseFactory();
            case CONDITIONAL -> new ConditionalResponseFactory();
            case RETRIEVER -> new RetrieverResponseFactory();
            case VARIABLE_ASSIGNER -> new VariableAssignerResponseFactory();
        };
    }

}
