package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.factory.create.*;
import org.springframework.stereotype.Component;


@Component
public class NodeFactoryProvider {

    public NodeFactory getFactory(NodeType type) {
        return switch (type) {
            case START -> new StartFactory();
            case LLM -> new LlmFactory();
            case ANSWER -> new AnswerFactory();
            case QUESTION_CLASSIFIER -> new QuestionClassifierFactory();
            case CONDITIONAL -> new ConditionalFactory();
            case RETRIEVER -> new RetrieverFactory();
            case VARIABLE_ASSIGNER -> new VariableAssignerFactory();
        };
    }

}
