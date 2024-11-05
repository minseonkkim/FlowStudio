package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.domain.node.entity.Conditional;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.factory.*;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class NodeFactoryProviderTest extends IntegrationTestSupport {

    @Autowired
    private NodeFactoryProvider factoryProvider;

    @DisplayName("NodeFactoryProvider를 통해 NodeType에 해당하는 NodeFactory를 반환한다.")
    @Test
    void getFactory() {
        // when
        NodeFactory factory1 = factoryProvider.getFactory(NodeType.START);
        NodeFactory factory2 = factoryProvider.getFactory(NodeType.LLM);
        NodeFactory factory3 = factoryProvider.getFactory(NodeType.QUESTION_CLASSIFIER);
        NodeFactory factory4 = factoryProvider.getFactory(NodeType.ANSWER);
        NodeFactory factory5 = factoryProvider.getFactory(NodeType.CONDITIONAL);
        NodeFactory factory6 = factoryProvider.getFactory(NodeType.RETRIEVER);
        NodeFactory factory7 = factoryProvider.getFactory(NodeType.VARIABLE_ASSIGNER);

        // then
        assertThat(factory1).isInstanceOf(StartFactory.class);
        assertThat(factory2).isInstanceOf(LlmFactory.class);
        assertThat(factory3).isInstanceOf(QuestionClassifierFactory.class);
        assertThat(factory4).isInstanceOf(AnswerFactory.class);
        assertThat(factory5).isInstanceOf(ConditionalFactory.class);
        assertThat(factory6).isInstanceOf(RetrieverFactory.class);
        assertThat(factory7).isInstanceOf(VariableAssignerFactory.class);
    }

}