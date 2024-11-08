package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.node.entity.*;
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

@ActiveProfiles("test")
@Transactional
class RetrieverExecutorTest extends IntegrationTestSupport {

    @Autowired
    private RetrieverExecutor retrieverExecutor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @DisplayName("Retriever 노드 실행")
    @Test
    void execute() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("test")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(0)
                .y(0)
                .build();

        Knowledge knowledge = Knowledge.builder()
                .id(3L)
                .user(user)
                .isPublic(true)
                .title("문서예시.txt")
                .totalToken(10)
                .build();

        Retriever retrieverNode = Retriever.builder()
                .id(1L)
                .chatFlow(chatFlow)
                .name("Retriever")
                .type(NodeType.RETRIEVER)
                .coordinate(coordinate)
                .intervalTime(2)
                .topK(3)
                .scoreThreshold(0.1f)
                .query("미국")
                .knowledge(knowledge)
                .build();

        Answer answer = Answer.create(chatFlow, coordinate);

        chatFlow.addNode(retrieverNode);
        chatFlow.addNode(answer);


        Chat chat = Chat.builder()
                .id(77L)
                .isPreview(true)
                .messageList("[]")
                .chatFlow(chatFlow)
                .user(user)
                .build();

        userRepository.save(user);

        redisService.save(chat.getId(), ChatEnvVariable.INPUT_MESSAGE, "보안 개발");

        // when
        retrieverExecutor.execute(retrieverNode, chat);
    }
}
