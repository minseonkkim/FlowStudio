package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.factory.copy.*;
import org.springframework.stereotype.Component;

@Component
public class NodeCopyFactoryProvider {
    public NodeCopyFactory getCopyFactory(NodeType type) {
        return switch (type) {
            case START -> new StartCopyFactory();
            case LLM -> new LlmCopyFactory();
            case ANSWER -> new AnswerCopyFactory();
            case QUESTION_CLASSIFIER -> new QuestionClassifierCopyFactory();
            case RETRIEVER -> new RetrieverCopyFactory();
            case CONDITIONAL -> null;
            case VARIABLE_ASSIGNER -> null;
        };
    }
}
