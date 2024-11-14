package com.ssafy.flowstudio.api.service.chatflow;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowRequest;
import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowUpdateResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.CategoryRepository;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.node.factory.create.QuestionClassifierFactory;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
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
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ActiveProfiles("test")
class ChatFlowServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private ChatFlowService chatFlowService;

    @DisplayName("공유여부가 false인 유저의 챗플로우 목록을 조회한다")
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
        List<ChatFlowListResponse> response = chatFlowService.getChatFlows(user, false);

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response).isNotNull()
                .extracting("chatFlowId", "title")
                .containsExactlyInAnyOrder(
                        tuple(chatFlow1.getId(), chatFlow1.getTitle()),
                        tuple(chatFlow2.getId(), chatFlow2.getTitle())
                );
    }

    @DisplayName("유저가 공유여부가 true, false인 챗플로우들을 모두 보유하고 있을 때 공유여부가 false인 유저의 챗플로우 목록만 조회한다")
    @Test
    void getPrivateChatFlows() {
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
                .title("title2")
                .isPublic(true)
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
        List<ChatFlowListResponse> response = chatFlowService.getChatFlows(user, false);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response).isNotNull()
                .extracting("chatFlowId", "title")
                .containsExactlyInAnyOrder(
                        tuple(chatFlow1.getId(), chatFlow1.getTitle())
                );
    }

    @DisplayName("유저가 공유여부가 true, false인 챗플로우들을 모두 보유하고 있을 때 공유여부가 true인 유저의 챗플로우 목록을 조회한다")
    @Test
    void getSharedChatFlows() {
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
                .title("title2")
                .isPublic(true)
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
        List<ChatFlowListResponse> response = chatFlowService.getChatFlows(user, true);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response).isNotNull()
                .extracting("chatFlowId", "title")
                .contains(
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

    @DisplayName("Retriever, Answer 노드를 가진 챗플로우를 업로드한다.")
    @Test
    void uploadChatFlowsWithPublicKnowledge() {
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

        Retriever retriever = Retriever.builder()
                .name("my-name")
                .chatFlow(chatFlow)
                .coordinate(coordinate)
                .type(NodeType.RETRIEVER)
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

        // 복제된 ChatFlow는 원본 ChatFlow와 ID가 다르다.
        assertThat(chatFlow.getId()).isNotEqualTo(chatFlowResponse.getChatFlowId());
        assertThat(chatFlowResponse).isNotNull();

        // 복제된 ChatFlow는 원본 ChatFlow가 가진 노드를 함께 복제한다.
        List<NodeResponse> clonedNodes = chatFlowResponse.getNodes();
        assertThat(clonedNodes).size().isEqualTo(2);
        assertThat(clonedNodes).extracting(NodeResponse::getType)
                .contains(NodeType.RETRIEVER, NodeType.ANSWER);

        // 복제된 Retriever 노드를 불러온다.
        NodeResponse clonedRetrieverResponse = chatFlowResponse.getNodes().stream()
                .filter(node -> NodeType.RETRIEVER.equals(node.getType()))
                .findFirst()
                .orElse(null);
        Retriever clonedRetriever = (Retriever) nodeRepository.findById(clonedRetrieverResponse.getNodeId()).orElse(null);
        em.refresh(clonedRetriever);

        // 복제된 Retriever 노드는 원본 Retriever 노드와 ID가 다르다.
//        assertThat(clonedRetriever.getId()).isNotEqualTo(retriever.getId());

        // 복제된 Knowledge는 원본 Knowledge와 ID만 다르고 다른 내용은 같아야 한다.
//        assertThat(clonedRetriever.getKnowledge().getId()).isNotEqualTo(knowledge.getId());
//        assertThat(clonedRetriever.getKnowledge().getTitle()).isEqualTo(knowledge.getTitle());
//        assertThat(clonedRetriever.getKnowledge().getTotalToken()).isEqualTo(knowledge.getTotalToken());
    }

    @DisplayName("Retriever, Answer 노드, 비공개된 Knowledge를 가진 챗플로우를 업로드한다.")
    @Test
    void uploadChatFlowsWithSecretKnowledge() {
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
                .isPublic(false)
                .totalToken(10)
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

        // 복제된 ChatFlow는 원본 ChatFlow와 ID가 다르다.
        assertThat(chatFlow.getId()).isNotEqualTo(chatFlowResponse.getChatFlowId());
        assertThat(chatFlowResponse).isNotNull();

        // 복제된 ChatFlow는 원본 ChatFlow가 가진 노드를 함께 복제한다.
        List<NodeResponse> clonedNodes = chatFlowResponse.getNodes();
        assertThat(clonedNodes).size().isEqualTo(2);
        assertThat(clonedNodes).extracting(NodeResponse::getType)
                .contains(NodeType.RETRIEVER, NodeType.ANSWER);

        // 복제된 Retriever 노드를 불러온다.
        NodeResponse clonedRetrieverResponse = chatFlowResponse.getNodes().stream()
                .filter(node -> NodeType.RETRIEVER.equals(node.getType()))
                .findFirst()
                .orElse(null);
        Retriever clonedRetriever = (Retriever) nodeRepository.findById(clonedRetrieverResponse.getNodeId()).orElse(null);
        em.refresh(clonedRetriever);

        // 복제된 Retriever 노드는 원본 Retriever 노드와 ID가 다르다.
        assertThat(clonedRetriever.getId()).isNotEqualTo(retriever.getId());

        // 복제된 Retriever 노드는 참조하는 Knowledge가 없다.
        assertThat(clonedRetriever.getKnowledge()).isNull();
    }

    @DisplayName("QuestionClassifier, Answer 노드, 간선을 가진 챗플로우를 업로드한다.")
    @Test
    void uploadChatFlowsWithQuestionClassifier() {
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
        ChatFlowResponse chatFlowResponse = chatFlowService.uploadChatFlow(user, chatFlow.getId());

        // then

        // 복제된 ChatFlow는 원본 ChatFlow와 ID가 다르다.
        ChatFlow clonedChatFlow = chatFlowRepository.findById(chatFlowResponse.getChatFlowId()).orElse(null);

        assertThat(clonedChatFlow).isNotNull();
        assertThat(chatFlow.getId()).isNotEqualTo(clonedChatFlow.getId());

        // 복제된 ChatFlow는 공유 여부가 true다.
        assertThat(clonedChatFlow.isPublic()).isTrue();

        // 복제된 ChatFlow는 원본 ChatFlow가 가진 노드를 함께 복제한다.
        List<NodeResponse> clonedNodes = chatFlowResponse.getNodes();
        assertThat(clonedNodes).size().isEqualTo(3);
        assertThat(clonedNodes).extracting(NodeResponse::getType)
                .contains(NodeType.QUESTION_CLASSIFIER, NodeType.ANSWER);

        // 복제된 노드들은 원본 노드들과 ID가 다르다.
        List<Long> originalIds = chatFlow.getNodes().stream()
                .map(Node::getId)
                .toList();
        List<Long> clonedIds = clonedNodes.stream()
                .map(NodeResponse::getNodeId)
                .toList();
        assertThat(clonedIds).doesNotContainAnyElementsOf(originalIds);

        // 복제된 QuestionClassifier 노드는 원본 QuestionClassifier 노드와 ID가 다르다.
        NodeResponse clonedQuestionClassifierResponse = clonedNodes.stream()
                .filter(node -> NodeType.QUESTION_CLASSIFIER.equals(node.getType()))
                .findFirst()
                .orElse(null);
        QuestionClassifier clonedQuestionClassifier = (QuestionClassifier) nodeRepository.findById(clonedQuestionClassifierResponse.getNodeId()).orElse(null);
        em.refresh(clonedQuestionClassifier);

        assertThat(clonedQuestionClassifier).isNotNull();
        assertThat(clonedQuestionClassifier.getId()).isNotEqualTo(questionClassifier.getId());

        // 복제된 QuestionClassifier 노드도 원본 QuestionClassifier 노드와 같이 2개의 QuestionClass를 가진다.
        assertThat(clonedQuestionClassifier.getQuestionClasses()).hasSize(2);

        // 복제된 2개의 QuestionClass는 2개의 원본 QuestionClass과 ID는 다르지만 내용은 같다.
        List<Long> originalQuestionClassIds = questionClassifier.getQuestionClasses().stream().map(QuestionClass::getId).toList();
        List<Long> clonedQuestionClassIds = clonedQuestionClassifier.getQuestionClasses().stream().map(QuestionClass::getId).toList();

        assertThat(originalQuestionClassIds).doesNotContainAnyElementsOf(clonedQuestionClassIds);
        assertThat(clonedQuestionClassifier.getQuestionClasses()).extracting(QuestionClass::getContent)
                .contains("question-class-1", "question-class-2");

        // 복제된 ChatFlow는 원본 ChatFlow가 가진 간선을 함께 복제한다.
        List<Edge> clonedEdges = edgeRepository.findByChatFlowId(clonedChatFlow.getId());
        assertThat(clonedEdges).isNotNull();

        // 복제된 간선은 원본 간선과 ID가 다르다.
        List<Long> originalEdgeIds = List.of(edge1.getId(), edge2.getId());
        List<Long> clonedEdgeIds = clonedEdges.stream().map(Edge::getId).toList();
        assertThat(originalEdgeIds).doesNotContainAnyElementsOf(clonedEdgeIds);

        // 복제된 간선은 복제된 질문 분류를 sourceConditionId로 갖고있다.
        assertThat(clonedEdges.stream().map(Edge::getSourceConditionId).toList())
                .containsExactlyInAnyOrderElementsOf(clonedQuestionClassIds);

        // 복제된 간선들은 복제된 questionClassifier를 출처 노드로 갖고있다.
        assertThat(clonedEdges.stream().map(Edge::getSourceNode).toList())
                .containsExactlyInAnyOrder(clonedQuestionClassifier, clonedQuestionClassifier);

        // 복제된 간선들은 복제된 answer1, answer2를 출처 노드로 갖고있다.
        List<NodeResponse> clonedAnswers = clonedNodes.stream()
                .filter(node -> NodeType.ANSWER.equals(node.getType())).toList();

        assertThat(clonedAnswers).isNotEmpty();
        assertThat(clonedEdges.stream().map(Edge::getTargetNode).toList())
                .extracting("id")
                .containsExactlyInAnyOrderElementsOf(clonedAnswers.stream().map(NodeResponse::getNodeId).toList());
    }

    @DisplayName("이미 게시된 상태의 챗플로우를 업로드하면 예외가 발생한다.")
    @Test
    void throwExeptionWhenUploadPublicChatFlow() {
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
                .isPublic(true)
                .build();

        chatFlowRepository.save(chatFlow);

        // when & then
        assertThatThrownBy(() -> chatFlowService.uploadChatFlow(user, chatFlow.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(ErrorCode.UPLOADED_CHAT_FLOW_CANNOT_BE_SHARED.getMessage());
    }

    @DisplayName("타인이 원작자인 챗플로우를 업로드하면 예외가 발생한다.")
    @Test
    void throwExeptionWhenUploadOthersChatFlow() {
        // given
        User user1 = User.builder()
                .username("test1")
                .build();

        User user2 = User.builder()
                .username("test2")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Coordinate coordinate = Coordinate.builder()
                .x(777)
                .y(777)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user1)
                .author(user1)
                .title("my-chatflow")
                .description("my-chatflow-description")
                .isPublic(true)
                .build();

        chatFlowRepository.save(chatFlow);

        // when & then
        assertThatThrownBy(() -> chatFlowService.uploadChatFlow(user2, chatFlow.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(ErrorCode.FORBIDDEN.getMessage());
    }

    @DisplayName("게시된 챗플로우를 다운로드한다.")
    @Test
    void downloadChatFlow() {
        // given
        User user1 = User.builder()
                .username("test1")
                .build();

        User user2 = User.builder()
                .username("test2")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Coordinate coordinate = Coordinate.builder()
                .x(777)
                .y(777)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user1)
                .author(user1)
                .title("my-chatflow")
                .description("my-chatflow-description")
                .isPublic(true)
                .build();

        chatFlowRepository.save(chatFlow);

        // when
        ChatFlowResponse chatFlowResponse = chatFlowService.downloadChatFlow(user2, chatFlow.getId());

        // then
        ChatFlow clonedChatFlow = chatFlowRepository.findById(chatFlowResponse.getChatFlowId()).orElse(null);

        assertThat(clonedChatFlow).isNotNull();
        assertThat(clonedChatFlow.getId()).isNotEqualTo(chatFlow.getId());
        assertThat(clonedChatFlow.getOwner().getId()).isEqualTo(user2.getId());
        assertThat(clonedChatFlow.getAuthor().getId()).isEqualTo(user1.getId());
        assertThat(clonedChatFlow.isPublic()).isFalse();
    }

    @DisplayName("게시된 챗플로우를 다운로드하면 원본 챗플로우의 shareCount가 증가한다.")
    @Test
    void incrementShareCount() {
        // given
        User user1 = User.builder()
                .username("test1")
                .build();

        User user2 = User.builder()
                .username("test2")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Coordinate coordinate = Coordinate.builder()
                .x(777)
                .y(777)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user1)
                .author(user1)
                .title("my-chatflow")
                .description("my-chatflow-description")
                .isPublic(true)
                .shareCount(0)
                .build();

        chatFlowRepository.save(chatFlow);

        // when
        ChatFlowResponse chatFlowResponse1 = chatFlowService.downloadChatFlow(user2, chatFlow.getId());
        ChatFlowResponse chatFlowResponse2 = chatFlowService.downloadChatFlow(user2, chatFlow.getId());

        // then
        ChatFlow clonedChatFlow1 = chatFlowRepository.findById(chatFlowResponse1.getChatFlowId()).orElse(null);
        ChatFlow clonedChatFlow2 = chatFlowRepository.findById(chatFlowResponse2.getChatFlowId()).orElse(null);

        assertThat(clonedChatFlow1).isNotNull();
        assertThat(clonedChatFlow2).isNotNull();
        assertThat(clonedChatFlow1.getShareCount()).isEqualTo(0);
        assertThat(clonedChatFlow2.getShareCount()).isEqualTo(0);
        assertThat(chatFlow.getShareCount()).isEqualTo(2);
    }

    @DisplayName("공유 여부가 true인 모든 챗플로우를 불러온다.")
    @Test
    void getEveryoneChatFlows() {
        // given
        User user1 = User.builder()
                .username("test1")
                .build();

        User user2 = User.builder()
                .username("test2")
                .build();

        User user3 = User.builder()
                .username("test3")
                .build();

        ChatFlow chatFlow1 = ChatFlow.builder()
                .owner(user1)
                .author(user1)
                .title("title1")
                .description("description")
                .build();
        ChatFlow chatFlow2 = ChatFlow.builder()
                .owner(user2)
                .author(user2)
                .title("title2")
                .isPublic(true)
                .description("description")
                .build();
        ChatFlow chatFlow3 = ChatFlow.builder()
                .owner(user3)
                .author(user3)
                .title("title3")
                .isPublic(true)
                .description("description")
                .build();

        userRepository.saveAll(List.of(user1, user2, user3));
        chatFlowRepository.saveAll(List.of(chatFlow1, chatFlow2, chatFlow3));

        // when
        List<ChatFlowListResponse> response = chatFlowService.getEveryoneChatFlows();

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response).isNotNull()
                .extracting("chatFlowId", "title")
                .contains(
                        tuple(chatFlow2.getId(), chatFlow2.getTitle()),
                        tuple(chatFlow3.getId(), chatFlow3.getTitle())
                );
    }
}