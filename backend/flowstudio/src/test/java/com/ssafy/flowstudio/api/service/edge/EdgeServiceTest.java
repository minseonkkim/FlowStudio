package com.ssafy.flowstudio.api.service.edge;

import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.edge.request.EdgeServiceRequest;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Start;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
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
                .build();

        // when
        EdgeResponse edgeResponse = edgeService.create(user, chatFlow.getId(), request);

        // then
        assertThat(edgeResponse).isNotNull()
                .extracting("sourceNodeId", "targetNodeId")
                .containsExactly(start.getId(), answer.getId());
    }

    @DisplayName("간선을 수정한다 응답이 반환된다.")
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

}