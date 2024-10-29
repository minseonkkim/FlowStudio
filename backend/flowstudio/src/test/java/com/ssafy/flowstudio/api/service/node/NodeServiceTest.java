package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

}