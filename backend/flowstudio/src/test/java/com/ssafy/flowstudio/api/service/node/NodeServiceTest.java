package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Start;
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

}
