package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.NodeDetailResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
class NodeServiceTest extends IntegrationTestSupport {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private NodeRepository nodeRepository;


    @DisplayName("Node를 생성하면 타입에 맞는 노드가 생성된다.")
    @Test
    void createNode() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title")
                .build();

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        NodeCreateServiceRequest request = NodeCreateServiceRequest.builder()
                .chatFlowId(chatFlow.getId())
                .coordinate(CoordinateServiceRequest.builder()
                        .x(1)
                        .y(1)
                        .build())
                .type(NodeType.START)
                .build();

        // when
        NodeCreateResponse response = nodeService.createNode(user, request);

        // then
        assertThat(response).isNotNull()
                .extracting("nodeType")
                .isEqualTo(NodeType.START);
    }

    @DisplayName("유효하지 않은 ChatFlow 아이디로 Node를 생성하면 예외가 발생한다.")
    @Test
    void createNodeWithoutInvalidChatFlow() {
        // given
        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        NodeCreateServiceRequest request = NodeCreateServiceRequest.builder()
                .chatFlowId(1L)
                .coordinate(CoordinateServiceRequest.builder()
                        .x(1)
                        .y(1)
                        .build())
                .type(NodeType.START)
                .build();

        userRepository.save(user);

        // when, then
        assertThatThrownBy(() -> nodeService.createNode(user, request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorCode.CHAT_FLOW_NOT_FOUND.getMessage());
    }

    @DisplayName("노드 아이디로 노드를 삭제한다.")
    @Test
    void deleteNode() {
        // given
        User user = User.builder()
                .username("test")
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

        Node node = Start.create(chatFlow, coordinate);

        chatFlow.getNodes().add(node);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        boolean result = nodeService.deleteNode(user, node.getId());

        // then
        assertTrue(result);
    }

    @DisplayName("없는 아이디로 노드를 삭제하면 예외가 발생한다.")
    @Test
    void deleteNodeWithInvalidNodeId() {
        // given
        User user = User.builder()
                .username("test")
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

        Node node = Start.create(chatFlow, coordinate);

        chatFlow.getNodes().add(node);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        assertThatThrownBy(() -> nodeService.deleteNode(user, node.getId() + 1))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorCode.NODE_NOT_FOUND.getMessage());
    }

    @DisplayName("내가만든 챗플로우가 아닌 챗플로우의 노드를 삭제하면 예외가 발생한다.")
    @Test
    void deleteNodeWithNotMyChatFlowOwner() {
        // given
        User user1 = User.builder()
                .username("test1")
                .build();

        User user2 = User.builder()
                .username("test2")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(1)
                .y(1)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user1)
                .author(user1)
                .title("title")
                .build();

        Node node = Start.create(chatFlow, coordinate);

        chatFlow.getNodes().add(node);

        userRepository.saveAll(List.of(user1, user2));
        chatFlowRepository.save(chatFlow);

        // when
        assertThatThrownBy(() -> nodeService.deleteNode(user2, node.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());
    }

    @DisplayName("선행 노드가 없는 노드를 상세 조회한다.")
    @Test
    void getFirstNode() {
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

        Node start = Start.create(chatFlow, coordinate);
        chatFlow.addNode(start);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        NodeDetailResponse startDetailResponse = nodeService.getNode(user, start.getId());

        // then
        assertThat(startDetailResponse).isNotNull();
        assertThat(startDetailResponse.getPrecedingNodes())
                .isEmpty();
        assertThat(startDetailResponse.getNodeId())
                .isEqualTo(start.getId());
    }

    @DisplayName("Answer 노드를 상세 조회한다.")
    @Test
    void getNode() {
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

        // Start와 QuestionClassifier 노드 생성
        Node start = Start.create(chatFlow, coordinate);
        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, coordinate);

        // Start와 QuestionClassifier를 잇는 간선 생성
        Edge edge1 = Edge.create(start, questionClassifier);
        questionClassifier.getInputEdges().add(edge1);
        start.getOutputEdges().add(edge1);

        // Answer 노드 생성
        Answer answer = Answer.create(chatFlow, coordinate);

        // QuestionClassifier와 Answer를 잇는 간선 생성
        Edge edge2 = Edge.create(questionClassifier, answer);
        answer.getInputEdges().add(edge2);
        questionClassifier.getOutputEdges().add(edge2);

        chatFlow.addNode(start);
        chatFlow.addNode(questionClassifier);
        chatFlow.addNode(answer);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        NodeDetailResponse answerDetailResponse = nodeService.getNode(user, answer.getId());

        // then
        assertThat(answerDetailResponse).isNotNull();
        assertThat(answerDetailResponse.getPrecedingNodes())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(SimpleNodeResponse.from(start), SimpleNodeResponse.from(questionClassifier));
        assertThat(answerDetailResponse.getNodeId())
                .isEqualTo(answer.getId());
    }


    @DisplayName("노드의 Input Edges를 순회하여 선행 노드들을 불러온다.")
    @Test
    void getPrecedingNodes() {
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

        // Start와 QuestionClassifier 노드 생성
        Node start = Start.create(chatFlow, coordinate);
        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, coordinate);

        // Start와 QuestionClassifier를 잇는 간선 생성
        Edge edge1 = Edge.create(start, questionClassifier);
        questionClassifier.getInputEdges().add(edge1);
        start.getOutputEdges().add(edge1);

        // Answer 노드 생성
        Answer answer = Answer.create(chatFlow, coordinate);

        // QuestionClassifier와 Answer를 잇는 간선 생성
        Edge edge2 = Edge.create(questionClassifier, answer);
        answer.getInputEdges().add(edge2);
        questionClassifier.getOutputEdges().add(edge2);

        chatFlow.addNode(start);
        chatFlow.addNode(questionClassifier);
        chatFlow.addNode(answer);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        List<Node> precedingNodes = nodeService.getPrecedingNodes(user, answer);

        // then
        assertThat(precedingNodes)
                .hasSize(2)
                .contains(start, questionClassifier);
    }
}
