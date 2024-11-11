package com.ssafy.flowstudio.domain.knowledge.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
class KnowledgeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @DisplayName("특정 챗플로우에서 사용하는 지식을 조회한다.")
    @Test
    void findByChatFlowId() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        userRepository.save(user);

        Knowledge knowledge = Knowledge.builder()
                .user(user)
                .title("title")
                .build();

        knowledgeRepository.save(knowledge);

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title")
                .description("description")
                .build();

        Retriever retriever = Retriever.builder()
                .name("name")
                .chatFlow(chatFlow)
                .type(NodeType.RETRIEVER)
                .knowledge(knowledge)
                .build();

        chatFlow.addNode(retriever);
        chatFlowRepository.save(chatFlow);

        // when
        List<Knowledge> result = knowledgeRepository.findByChatFlowId(chatFlow.getId());

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(knowledge.getTitle(), result.get(0).getTitle());
    }
}