package com.ssafy.flowstudio.api.service.edge;

import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.edge.request.EdgeServiceRequest;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.node.factory.create.QuestionClassifierFactory;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
class EdgeServiceTest extends IntegrationTestSupport {

    @Autowired
    private EdgeService edgeService;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("간선을 만들면 응답이 반환된다.")
    @Test
    void createEdge() {
        // given
        User user = User.builder()
                .username("test")
                .apiKey(ApiKey.empty())
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .author(user)
                .owner(user)
                .title("test")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(1)
                .y(1)
                .build();

        Start start = Start.create(chatFlow, coordinate);
        Answer answer = Answer.create(chatFlow, coordinate);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);
        nodeRepository.saveAll(List.of(start, answer));

        EdgeServiceRequest request = EdgeServiceRequest.builder()
                .sourceNodeId(start.getId())
                .targetNodeId(answer.getId())
                .sourceConditionId(0L)
                .build();

        // when
        EdgeResponse edgeResponse = edgeService.create(user, chatFlow.getId(), request);

        // then
        assertThat(edgeResponse).isNotNull()
                .extracting("sourceNodeId", "targetNodeId")
                .containsExactly(start.getId(), answer.getId());
    }

    @DisplayName("Cycle이 감지되면 간선을 생성할 수 없다.")
    @Test
    void createEdgeWithCycle() {
        // given
        User user = User.builder()
                .username("test1")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(1)
                .y(1)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title")
                .build();

        // Start와 Answer 노드 생성
        Node start = Start.create(chatFlow, coordinate);
        Answer answer = Answer.create(chatFlow, coordinate);

        chatFlow.addNode(start);
        chatFlow.addNode(answer);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // Start와 Answer를 잇는 간선을 생성
        Edge edge = Edge.create(start, answer);
        edgeRepository.save(edge);
        answer.getInputEdges().add(edge);
        start.getOutputEdges().add(edge);

        EdgeServiceRequest request = EdgeServiceRequest.builder()
                .sourceConditionId(0L)
                .sourceNodeId(answer.getId())
                .targetNodeId(start.getId())
                .build();

        // when & then
        assertThatThrownBy(() -> edgeService.create(user, chatFlow.getId(), request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorCode.CHAT_FLOW_CYCLE_DETECTED.getMessage());
    }

    @DisplayName("간선을 수정하면 응답이 반환된다.")
    @Test
    void updateEdge() {
        // given
        User user = User.builder()
                .username("test")
                .apiKey(ApiKey.empty())
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .author(user)
                .owner(user)
                .title("test")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(1)
                .y(1)
                .build();

        Start start = Start.create(chatFlow, coordinate);
        Answer answer = Answer.create(chatFlow, coordinate);

        Edge edge = Edge.builder()
                .sourceNode(start)
                .targetNode(answer)
                .build();

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);
        nodeRepository.saveAll(List.of(start, answer));
        edgeRepository.save(edge);

        EdgeServiceRequest request = EdgeServiceRequest.builder()
                .sourceNodeId(start.getId())
                .targetNodeId(answer.getId())
                .sourceConditionId(1L)
                .build();

        // when
        edgeService.update(user, chatFlow.getId(), edge.getId(), request);

        // then
        assertThat(edge.getSourceNode().getId()).isEqualTo(start.getId());
        assertThat(edge.getTargetNode().getId()).isEqualTo(answer.getId());
        assertThat(edge.getSourceConditionId()).isEqualTo(1L);
    }

    @DisplayName("간선을 삭제하면 true가 반환된다.")
    @Test
    void deleteEdge() {
        // given
        User user = User.builder()
                .username("test")
                .apiKey(ApiKey.empty())
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .author(user)
                .owner(user)
                .title("test")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(1)
                .y(1)
                .build();

        Start start = Start.create(chatFlow, coordinate);
        Answer answer = Answer.create(chatFlow, coordinate);

        Edge edge = Edge.builder()
                .sourceNode(start)
                .targetNode(answer)
                .build();

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);
        nodeRepository.saveAll(List.of(start, answer));
        edgeRepository.save(edge);

        // when
        boolean result = edgeService.delete(user, chatFlow.getId(), edge.getId());

        // then
        assertTrue(result);
    }

    @DisplayName("질문분류기가 아닌 노드에 간선을 한개 이상 연결하면 예외가 발생한다.")
    @Test
    void createMultipleEdges() {
        // given
        User user = User.builder()
                .username("test")
                .apiKey(ApiKey.empty())
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .author(user)
                .owner(user)
                .title("test")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(1)
                .y(1)
                .build();

        Start start = Start.create(chatFlow, coordinate);
        Answer answer = Answer.create(chatFlow, coordinate);

        Edge edge = Edge.builder()
                .sourceNode(start)
                .targetNode(answer)
                .build();

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);
        nodeRepository.saveAll(List.of(start, answer));
        edgeRepository.save(edge);
        entityManager.clear();

        EdgeServiceRequest request = EdgeServiceRequest.builder()
                .sourceNodeId(start.getId())
                .targetNodeId(answer.getId())
                .build();

        // when & then
        assertThatThrownBy(() -> edgeService.create(user, chatFlow.getId(), request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(ErrorCode.MULTIPLE_EDGE_FORBIDDEN.getMessage());
    }

    @DisplayName("질문분류기에 질문 분류 갯수 이상의 간선을 연결하면 예외가 발생한다.")
    @Test
    void createMultipleEdgesToQuestionClassifier() {
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

        Answer answer3 = Answer.builder()
                .name("my-answer-3")
                .chatFlow(chatFlow)
                .coordinate(coordinate)
                .type(NodeType.ANSWER)
                .outputMessage("my-answer-3")
                .build();

        chatFlow.addNode(answer1);
        chatFlow.addNode(answer2);
        chatFlow.addNode(answer3);

        chatFlowRepository.save(chatFlow);

        Edge edge1 = Edge.create(questionClassifier, answer1, questionClasses.get(0).getId());
        Edge edge2 = Edge.create(questionClassifier, answer2, questionClasses.get(1).getId());

        edgeRepository.save(edge1);
        edgeRepository.save(edge2);

        entityManager.clear();

        EdgeServiceRequest request1 = EdgeServiceRequest.builder()
                .sourceNodeId(questionClassifier.getId())
                .targetNodeId(answer3.getId())
                .sourceConditionId(questionClasses.get(0).getId())
                .build();

        EdgeServiceRequest request2 = EdgeServiceRequest.builder()
                .sourceNodeId(questionClassifier.getId())
                .targetNodeId(answer3.getId())
                .sourceConditionId(questionClasses.get(1).getId())
                .build();

        // when & then
        assertThatThrownBy(() -> edgeService.create(user, chatFlow.getId(), request1))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(ErrorCode.MULTIPLE_EDGE_FORBIDDEN.getMessage());

        assertThatThrownBy(() -> edgeService.create(user, chatFlow.getId(), request2))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(ErrorCode.MULTIPLE_EDGE_FORBIDDEN.getMessage());
    }
}