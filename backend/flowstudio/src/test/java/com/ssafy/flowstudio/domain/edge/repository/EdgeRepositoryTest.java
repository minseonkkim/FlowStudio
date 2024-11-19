package com.ssafy.flowstudio.domain.edge.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.node.factory.create.QuestionClassifierFactory;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@Transactional
class EdgeRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;
    @DisplayName("특정 챗플로우에 포함된 간선을 조회한다.")
    @Test
    void findByChatFlowId() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        userRepository.save(user);

        Coordinate coordinate = Coordinate.builder()
                .x(777)
                .y(777)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("my-chatflow")
                .description("my-chatflow-description")
                .build();

        QuestionClassifierFactory questionClassifierFactory = new QuestionClassifierFactory();
        QuestionClassifier questionClassifier = (QuestionClassifier) questionClassifierFactory.createNode(chatFlow, coordinate);

        List<QuestionClass> questionClasses = questionClassifier.getQuestionClasses();
        questionClasses.get(0).update("question-class-1");
        questionClasses.get(1).update("question-class-2");

        chatFlow.addNode(questionClassifier);

        Answer answer1 = Answer.builder()
                .name("my-answer-1")
                .chatFlow(chatFlow)
                .coordinate(coordinate)
                .type(NodeType.ANSWER)
                .outputMessage("my-answer-1")
                .build();

        Answer answer2 = Answer.builder()
                .name("my-answer-2")
                .chatFlow(chatFlow)
                .coordinate(coordinate)
                .type(NodeType.ANSWER)
                .outputMessage("my-answer-2")
                .build();

        chatFlow.addNode(answer1);
        chatFlow.addNode(answer2);

        chatFlowRepository.save(chatFlow);

        Edge edge1 = Edge.create(questionClassifier, answer1, questionClasses.get(0).getId());
        Edge edge2 = Edge.create(questionClassifier, answer2, questionClasses.get(1).getId());

        edgeRepository.save(edge1);
        edgeRepository.save(edge2);

        // when
        List<Edge> edges = edgeRepository.findByChatFlowId(chatFlow.getId());

        // then
        assertThat(edges).hasSize(2)
                .containsExactly(edge1, edge2);
    }
}