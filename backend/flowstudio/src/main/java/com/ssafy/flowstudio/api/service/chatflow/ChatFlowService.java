package com.ssafy.flowstudio.api.service.chatflow;

import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.CategoryResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowUpdateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.NodeCopyFactoryProvider;
import com.ssafy.flowstudio.api.service.rag.VectorStoreService;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.util.MessageParseUtil;
import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlowCategory;
import com.ssafy.flowstudio.domain.chatflow.repository.CategoryRepository;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.node.factory.copy.NodeCopyFactory;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.node.repository.QuestionClassRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatFlowService {

    private final ChatFlowRepository chatFlowRepository;
    private final CategoryRepository categoryRepository;
    private final KnowledgeRepository knowledgeRepository;
    private final NodeRepository nodeRepository;
    private final NodeCopyFactoryProvider nodeCopyFactoryProvider;
    private final EdgeRepository edgeRepository;
    private final QuestionClassRepository questionClassRepository;
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final VectorStoreService vectorStoreService;
    private final MessageParseUtil messageParseUtil;

    public List<ChatFlowListResponse> getEveryoneChatFlows() {
        List<ChatFlow> chatFlows = chatFlowRepository.findByIsPublicTrue();

        return chatFlows.stream()
                .map(chatFlow -> {
                    List<ChatFlowCategory> chatFlowCategories = chatFlow.getCategories();

                    List<Category> categories = chatFlowCategories.stream()
                            .map(chatFlowCategory -> categoryRepository.findById(chatFlowCategory.getCategory().getId())
                                    .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND)))
                            .toList();

                    return ChatFlowListResponse.of(chatFlow, categories);
                })
                .toList();
    }

    public List<ChatFlowListResponse> getChatFlows(User user, boolean isShared, boolean test) {
        List<ChatFlow> chatFlows;

        if (test) {
            chatFlows = chatFlowRepository.findByOwnerWithTest(user.getId());
        } else {
            chatFlows = chatFlowRepository.findByOwnerAndIsPublic(user, isShared);
        }

        return chatFlows.stream()
                .map(chatFlow -> {
                    List<ChatFlowCategory> chatFlowCategories = chatFlow.getCategories();

                    List<Category> categories = chatFlowCategories.stream()
                            .map(chatFlowCategory -> categoryRepository.findById(chatFlowCategory.getCategory().getId())
                                    .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND)))
                            .toList();

                    return ChatFlowListResponse.of(chatFlow, categories);
                })
                .toList();
    }

    public ChatFlowResponse getChatFlow(User user, Long chatFlowId) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId).orElseThrow(
                () -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        System.out.println(chatFlow.getNodes());

        List<EdgeResponse> edges = edgeRepository.findByChatFlowId(chatFlowId).stream()
                .map(EdgeResponse::from)
                .toList();

        return ChatFlowResponse.from(chatFlow, edges);
    }

    @Transactional
    public ChatFlowResponse createChatFlow(User user, ChatFlowServiceRequest request) {
        ChatFlow chatFlow = ChatFlow.create(user, user, request.getTitle(), request.getDescription(), request.getThumbnail());

        Node startNode = Start.create(chatFlow, Coordinate.create(870, 80));

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());

        chatFlow.updateCategories(categories);
        chatFlow.addNode(startNode);
        ChatFlow savedChatflow = chatFlowRepository.save(chatFlow);

        return ChatFlowResponse.from(savedChatflow, null);
    }

    @Transactional
    public boolean deleteChatFlow(User user, Long chatFlowId) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId).orElseThrow(
                () -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        if (chatFlow.isPublic() && !chatFlow.getAuthor().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        chatFlowRepository.delete(chatFlow);
        return true;
    }

    @Transactional
    public ChatFlowUpdateResponse updateChatFlow(User user, Long chatFlowId, ChatFlowServiceRequest request) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId).orElseThrow(
                () -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        chatFlow.update(request.getTitle(), request.getDescription(), request.getThumbnail(), categories);

        return ChatFlowUpdateResponse.of(chatFlow, categories);
    }

    @Transactional
    public ChatFlowResponse createExampleChatFlow(User user) {
        // 노드 생성
        ChatFlow chatFlow = ChatFlow.create(user, user, "example title", "example description", "1");
        Start start = Start.create(chatFlow, Coordinate.create(870, 80));
        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, Coordinate.create(970, 80));
        Retriever retriever = Retriever.create(chatFlow, Coordinate.create(970, 80), 1, 3, 0);
        LLM llm = LLM.create(chatFlow, Coordinate.create(970, 80));
        Answer answer = Answer.create(chatFlow, Coordinate.create(970, 80));

        // 챗플로우에 노드 추가
        chatFlow.addNode(start);
        chatFlow.addNode(questionClassifier);
        chatFlow.addNode(retriever);
        chatFlow.addNode(llm);
        chatFlow.addNode(answer);

        // 질문분류기에 질문 분류 추가
        QuestionClass questionClass = QuestionClass.empty();
        questionClass.update("질문 분류");
        questionClass.updateQuestionClassifier(questionClassifier);
        questionClassifier.addQuestionClass(questionClass);

        // 챗플로우 저장
        ChatFlow savedChatflow = chatFlowRepository.save(chatFlow);

        // 노드 업데이트
        Long retrieverId = retriever.getId();
        Long llmId = llm.getId();

        llm.updatePrompt("아래 글에 기반해서 대답해줘\n \\n\\n{{" + retrieverId + "}}", "{{INPUT_MESSAGE}}");
        answer.updateOutputMessage("{{" + llmId + "}}");

        // 노드 연결
        Edge edge1 = Edge.create(start, questionClassifier);
        Edge edge2 = Edge.create(questionClassifier, retriever, questionClass.getId());
        Edge edge3 = Edge.create(retriever, llm);
        Edge edge4 = Edge.create(llm, answer);

        // 간선 저장
        List<Edge> edges = edgeRepository.saveAll(List.of(edge1, edge2, edge3, edge4));

        List<EdgeResponse> edgeResponses = edges.stream().map(EdgeResponse::from).toList();
        return ChatFlowResponse.from(savedChatflow, edgeResponses);
    }

    @Transactional
    public ChatFlowResponse uploadChatFlow(User user, Long chatFlowId) {
        ChatFlow clonedChatFlow = copyChatFlow(user.getId(), chatFlowId, true);
        List<EdgeResponse> newEdges = edgeRepository.findByChatFlowId(clonedChatFlow.getId()).stream().map(EdgeResponse::from).toList();

        return ChatFlowResponse.from(clonedChatFlow, newEdges);
    }

    @Transactional
    public ChatFlowResponse downloadChatFlow(User user, Long chatFlowId) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId).orElseThrow(
                () -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        ChatFlow clonedChatFlow = copyChatFlow(user.getId(), chatFlowId, false);
        List<EdgeResponse> newEdges = edgeRepository.findByChatFlowId(clonedChatFlow.getId()).stream().map(EdgeResponse::from).toList();

        chatFlow.incrementShareCount();

        return ChatFlowResponse.from(clonedChatFlow, newEdges);
    }

    public ChatFlow copyChatFlow(Long userId, Long chatFlowId, boolean isUpload) {
        // ChatFlow를 조회한다.
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId).orElseThrow(
                () -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        User client = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_USER)
        );

        // 자신이 원작자인 챗봇만 업로드할 수 있다.
        if (isUpload && client.getId().longValue() != chatFlow.getAuthor().getId().longValue()) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        // 이미 게시된 상태의 챗플로우는 업로드할 수 없다.
        if (chatFlow.isPublic() && isUpload) {
            throw new BaseException(ErrorCode.UPLOADED_CHAT_FLOW_CANNOT_BE_SHARED);
        }

        // ChatFlow를 복제하여 DB에 저장한다.
        List<Category> categories = chatFlow.getCategories().stream().map(ChatFlowCategory::getCategory).toList();

        ChatFlow clonedChatFlow = ChatFlow.builder()
                .owner(client)
                .author(chatFlow.getAuthor())
                .title(chatFlow.getTitle())
                .description(chatFlow.getDescription())
                .thumbnail(chatFlow.getThumbnail())
                .isPublic(isUpload)
                .shareCount(0)
                .build();

        clonedChatFlow.updateCategories(categories);
        chatFlowRepository.save(clonedChatFlow);

        // 원본 지식 ID와 복제된 지식을 매핑시켜 줄 Map을 생성한다.
        Map<Long, Knowledge> knowledgeMap = new HashMap<>();

        // 해당 ChatFlow에서 사용하는 지식들을 불러온 후 공유 여부가 참이면 복제한다.
        List<Knowledge> knowledgeList = knowledgeRepository.findByChatFlowId(chatFlowId);
        for (Knowledge originalKnowledge : knowledgeList) {
            if (originalKnowledge.isPublic()) {
                KnowledgeResponse clonedKnowledgeResponse = vectorStoreService.copyDocument(client, originalKnowledge.getId());
                Knowledge clonedKnowledge = knowledgeRepository.findById(clonedKnowledgeResponse.getKnowledgeId()).orElseThrow(
                        () -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND)
                );

                // 복제된 지식을 DB에 저장 후 맵에 추가한다.
                knowledgeRepository.save(clonedKnowledge);
                knowledgeMap.put(originalKnowledge.getId(), clonedKnowledge);
            }
        }

        // 원본 노드 ID와 복제된 노드를 매핑시켜 줄 Map을 생성한다.
        Map<Long, Node> nodeMap = new HashMap<>();

        // 해당 ChatFlow에서 사용하는 노드들을 불러온 후 타입에 맞춰 복제한다.
        List<Node> nodeList = chatFlow.getNodes();

        for (Node originalNode : nodeList) {
            NodeCopyFactory factory = nodeCopyFactoryProvider.getCopyFactory(originalNode.getType());

            Node clonedNode;

            if (originalNode.getType() == NodeType.RETRIEVER) {
                // Retriever 노드라면 복제된 knowledge를 새로 매핑한다.
                Retriever originalRetriever = (Retriever) originalNode;
                Knowledge knowledge = originalRetriever.getKnowledge();
                if (knowledge != null && knowledge.isPublic()) {
                    clonedNode = factory.copyNode(originalNode, clonedChatFlow, knowledgeMap.get(knowledge.getId()));
                } else {
                    clonedNode = factory.copyNode(originalNode, clonedChatFlow);
                }
            } else if (originalNode.getType() == NodeType.LLM) {
                // Llm 노드라면 프롬프트 내의 변수에 복제된 노드의 ID를 새로 매핑한다.
                LLM originalLLM = (LLM) originalNode;
                String originalSystemPrompt = originalLLM.getPromptSystem();
                String originalUserPrompt = originalLLM.getPromptUser();
                String clonedSystemPrompt = messageParseUtil.replace(originalSystemPrompt, nodeMap);
                String clonedUserPrompt = messageParseUtil.replace(originalUserPrompt, nodeMap);
                clonedNode = factory.copyNode(originalNode, clonedChatFlow, clonedSystemPrompt, clonedUserPrompt);
            } else {
                clonedNode = factory.copyNode(originalNode, clonedChatFlow);
            }

            // 복제된 노드를 DB에 저장 후 맵에 추가한다.
            nodeRepository.save(clonedNode);
            nodeMap.put(originalNode.getId(), clonedNode);
        }

        // 원본 질문 분류 ID와 복제된 질문 분류를 매핑시켜 줄 Map을 생성한다.
        Map<Long, QuestionClass> questionClassMap = new HashMap<>();

        List<QuestionClass> questionClasses = questionClassRepository.findByChatFlowId(chatFlowId);
        for (QuestionClass originalQuestionClass : questionClasses) {
            QuestionClass clonedQuestionClass = QuestionClass.builder()
                    .content(originalQuestionClass.getContent())
                    .questionClassifier((QuestionClassifier) nodeMap.get(originalQuestionClass.getQuestionClassifier().getId()))
                    .build();

            // 복제된 질문 분류를 DB에 저장 후 맵에 추가한다.
            questionClassRepository.save(clonedQuestionClass);
            System.out.println("originalQuestionClass.getId() = " + originalQuestionClass.getId());
            questionClassMap.put(originalQuestionClass.getId(), clonedQuestionClass);
        }

        // 간선을 복제하여 저장한다.
        List<Edge> edges = edgeRepository.findByChatFlowId(chatFlowId);

        for (Edge originalEdge : edges) {
            Long sourceNodeId = originalEdge.getSourceNode().getId();
            Long targetNodeId = originalEdge.getTargetNode().getId();
            Long sourceConditionId = null;

            // 간선의 출처 노드가 질문 분류기면 위에서 복제된 질문 분류의 ID로 sourceConditionId를 새롭게 매핑한다.
            if (originalEdge.getSourceNode().getType() == NodeType.QUESTION_CLASSIFIER) {
                sourceConditionId = questionClassMap.get(originalEdge.getSourceConditionId()).getId();
            }

            Edge edge = Edge.create(
                    nodeMap.get(sourceNodeId),
                    nodeMap.get(targetNodeId),
                    sourceConditionId
            );

            edgeRepository.save(edge);
        }

        // DB와 동기화된 상태의 clonedChatFlow를 불러와 반환한다.
        entityManager.refresh(clonedChatFlow);

        return clonedChatFlow;
    }

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryResponse::from)
                .toList();
    }

}
