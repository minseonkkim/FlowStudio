package com.ssafy.flowstudio.api.service.chatflow;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowRequest;
import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowUpdateResponse;
import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.CategoryRepository;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ActiveProfiles("test")
class ChatFlowServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChatFlowService chatFlowService;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @DisplayName("챗플로우 목록을 조회한다")
    @Test
    void getChatFlows() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlow chatFlow1 = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title1")
                .description("description")
                .build();
        ChatFlow chatFlow2 = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title1")
                .description("description")
                .build();

        Category category1 = Category.builder()
                .name("category1")
                .build();
        Category category2 = Category.builder()
                .name("category1")
                .build();

        chatFlow1.updateCategories(List.of(category1, category2));

        userRepository.save(user);
        chatFlowRepository.saveAll(List.of(chatFlow1, chatFlow2));

        // when
        List<ChatFlowListResponse> response = chatFlowService.getChatFlows(user);

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response).isNotNull()
                .extracting("chatFlowId", "title")
                .containsExactlyInAnyOrder(
                        tuple(chatFlow1.getId(), chatFlow1.getTitle()),
                        tuple(chatFlow2.getId(), chatFlow2.getTitle())
                );
    }

    @DisplayName("챗플로우를 조회한다")
    @Test
    void getChatFlow() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title1")
                .description("description")
                .build();

        Node startNode = Start.create(chatFlow, Coordinate.create(870, 80));
        chatFlow.addNode(startNode);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // then
        ChatFlowResponse response = chatFlowService.getChatFlow(user, chatFlow.getId());

        // when
        assertThat(response).isNotNull()
                .extracting("title", "chatFlowId")
                .containsExactly(chatFlow.getTitle(), chatFlow.getId());
        assertThat(response.getNodes()).isNotNull()
                .extracting("nodeId", "name", "type")
                .containsExactlyInAnyOrder(
                        tuple(startNode.getId(), startNode.getName(), startNode.getType())
                );
    }

    @DisplayName("챗플로우를 생성한다")
    @Test
    void createChatFlow() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlowRequest request = ChatFlowRequest.builder()
                .title("title1")
                .description("description")
                .thumbnail("thumbnail")
                .categoryIds(List.of())
                .build();

        userRepository.save(user);

        // when
        ChatFlowResponse response = chatFlowService.createChatFlow(user, ChatFlowServiceRequest.from(request));

        // then
        assertThat(response).isNotNull()
                .extracting("chatFlowId", "title")
                .containsExactly(response.getChatFlowId(), response.getTitle());
        assertThat(response.getNodes().size()).isEqualTo(1);
    }

    @DisplayName("챗플로우를 수정한다")
    @Test
    void updateChatFlow() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title1")
                .description("description")
                .build();

        Category category1 = Category.builder()
                .name("category1")
                .build();

        Category category2 = Category.builder()
                .name("category2")
                .build();

        userRepository.save(user);
        categoryRepository.saveAll(List.of(category1, category2));
        chatFlowRepository.save(chatFlow);

        List<Long> categoryIds = List.of(category1.getId(), category2.getId());

        ChatFlowServiceRequest request = ChatFlowServiceRequest.builder()
                .title("updatedTitle")
                .thumbnail("updatedThumbnail2")
                .description("description2")
                .categoryIds(categoryIds)
                .build();

        // when
        ChatFlowUpdateResponse response = chatFlowService.updateChatFlow(user, chatFlow.getId(), request);

        // then
        assertThat(response).isNotNull()
                .extracting("title", "thumbnail", "description")
                .containsExactly("updatedTitle", "updatedThumbnail2", "description2");

        assertThat(response.getCategories()).isNotNull()
                .extracting("name")
                .containsExactlyInAnyOrder("category1", "category2");
    }

    @DisplayName("챗플로우를 삭제한다")
    @Test
    void deleteChatFlow() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title1")
                .description("description")
                .build();

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        boolean result = chatFlowService.deleteChatFlow(user, chatFlow.getId());

        // then
        assertTrue(result);

    }

    @DisplayName("챗플로우를 업로드한다.")
    @Test
    void uploadChatFlows() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        userRepository.save(user);

        Coordinate coordinate = Coordinate.builder()
                .x(777)
                .y(777)
                .build();

        Knowledge knowledge = Knowledge.builder()
                .user(user)
                .title("my-knowledge")
                .isPublic(true)
                .build();

        knowledgeRepository.save(knowledge);

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("my-chatflow")
                .description("my-chatflow-description")
                .build();

        Retriever retriever = Retriever.builder()
                .name("my-name")
                .chatFlow(chatFlow)
                .coordinate(coordinate)
                .type(NodeType.RETRIEVER)
                .knowledge(knowledge)
                .build();

        chatFlow.addNode(retriever);

        Answer answer = Answer.builder()
                .name("my-answer")
                .chatFlow(chatFlow)
                .coordinate(coordinate)
                .type(NodeType.ANSWER)
                .outputMessage("my-answer")
                .build();

        chatFlow.addNode(answer);
        chatFlowRepository.save(chatFlow);

        // when
        ChatFlowResponse chatFlowResponse = chatFlowService.uploadChatFlow(user, chatFlow.getId());

        // then
        assertThat(chatFlow.getId()).isNotEqualTo(chatFlowResponse.getChatFlowId());
        assertThat(chatFlowResponse).isNotNull();
        assertThat(chatFlowResponse.getNodes()).size().isEqualTo(2);

    }

}