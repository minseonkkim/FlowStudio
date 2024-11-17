package com.ssafy.flowstudio.api.service.rag;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.node.repository.RetrieverRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
class KnowledgeServiceTest extends IntegrationTestSupport {

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @DisplayName("지식을 삭제하면 리트리버의 지식이 삭제된다.")
    @Test
    void deleteKnowledge() {
        // given
        User user = User.builder()
                .username("username")
                .build();

        Knowledge knowledge = Knowledge.builder()
                .user(user)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .title("chatFlow")
                .author(user)
                .owner(user)
                .build();

        Retriever retriever = Retriever.builder()
                .name("retriever")
                .coordinate(Coordinate.create(0, 0))
                .type(NodeType.RETRIEVER)
                .chatFlow(chatFlow)
                .knowledge(knowledge)
                .build();

        chatFlow.addNode(retriever);

        userRepository.save(user);
        knowledgeRepository.save(knowledge);
        chatFlowRepository.save(chatFlow);

        // when
        boolean result = knowledgeService.deleteKnowledge(user, knowledge.getId());

        // then
        assertTrue(result);
        assertThat(retriever.getKnowledge()).isNull();
    }

}
