package com.ssafy.flowstudio.api.service.chatflow;

import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.*;
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
import groovy.lang.Tuple;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<ChatFlowListResponse> getEveryoneChatFlows(int page, int limit) {
        PageRequest pageable = PageRequest.of(page, limit, Sort.by("shareCount").descending());
        List<ChatFlow> chatFlows = chatFlowRepository.findByIsPublicTrue(pageable);

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

    public List<ChatFlowListResponse> getChatFlows(User user, boolean isShared, boolean test, int page, int limit) {
        PageRequest pageable = PageRequest.of(page, limit, Sort.by("shareCount").descending());
        List<ChatFlow> chatFlows;

        if (test) {
            chatFlows = chatFlowRepository.findByOwnerWithTest(user.getId(), pageable);
        } else {
            chatFlows = chatFlowRepository.findByOwnerAndIsPublic(user, isShared, pageable);
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

        // Private 상태의 다른 Owner의 Chatflow는 조회할 수 없다.
        if (!chatFlow.isPublic() && !chatFlow.getOwner().equals(user)) {
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
        ChatFlow chatFlow = ChatFlow.create(user, user, "너만의 챗봇을 만들어봐!", "동물, 식물 질문에 따라 말투가 바뀌는 챗봇", "1");
        Start start = Start.create(chatFlow, Coordinate.create(870, 80));
        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, Coordinate.create(1071, 45));
        LLM llm1 = LLM.create(chatFlow, "LLM 1", Coordinate.create(1295, -78));
        LLM llm2 = LLM.create(chatFlow, "LLM 2", Coordinate.create(1295, 90));
        Answer answer1 = Answer.create(chatFlow, "답변 1", Coordinate.create(1500, -78));
        Answer answer2 = Answer.create(chatFlow, "답변 2", Coordinate.create(1500, 90));

        // 챗플로우에 노드 추가
        chatFlow.addNode(start);
        chatFlow.addNode(questionClassifier);
        chatFlow.addNode(llm1);
        chatFlow.addNode(llm2);
        chatFlow.addNode(answer1);
        chatFlow.addNode(answer2);

        // 질문분류기에 질문 분류 추가
        QuestionClass questionClass1 = QuestionClass.empty();
        questionClass1.update("동물");
        questionClass1.updateQuestionClassifier(questionClassifier);
        questionClassifier.addQuestionClass(questionClass1);

        QuestionClass questionClass2 = QuestionClass.empty();
        questionClass2.update("식물");
        questionClass2.updateQuestionClassifier(questionClassifier);
        questionClassifier.addQuestionClass(questionClass2);

        // 챗플로우 저장
        ChatFlow savedChatflow = chatFlowRepository.save(chatFlow);

        // 노드 업데이트
        llm1.updatePrompt("존댓말을 사용해서 친절하게 답변해줘!", "{{INPUT_MESSAGE}}");
        llm2.updatePrompt("반말을 사용해서 친근하게 답변해줘!", "{{INPUT_MESSAGE}}");

        answer1.updateOutputMessage("{{" + llm1.getId() + "}}");
        answer2.updateOutputMessage("{{" + llm2.getId() + "}}");

        // 노드 연결
        Edge edge1 = Edge.create(start, questionClassifier);
        Edge edge2 = Edge.create(questionClassifier, llm1, questionClass1.getId());
        Edge edge3 = Edge.create(questionClassifier, llm2, questionClass2.getId());
        Edge edge4 = Edge.create(llm1, answer1);
        Edge edge5 = Edge.create(llm2, answer2);

        // 간선 저장
        List<Edge> edges = edgeRepository.saveAll(List.of(edge1, edge2, edge3, edge4, edge5));

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

        // 해당 ChatFlow에서 사용하는 노드들을 불러온다.
        List<Node> nodeList = chatFlow.getNodes();

        // 노드들을 타입에 맞춰 복제한다.
        for (Node originalNode : nodeList) {
            NodeCopyFactory factory = nodeCopyFactoryProvider.getCopyFactory(originalNode.getType());
            Node clonedNode;

            if (originalNode.getType() == NodeType.RETRIEVER) {
                // Retriever 노드라면 복제된 knowledge를 새로 매핑해서 복제한다.
                Retriever originalRetriever = (Retriever) originalNode;
                Knowledge knowledge = originalRetriever.getKnowledge();
                if (knowledge != null && knowledge.isPublic()) {
                    clonedNode = factory.copyNode(originalNode, clonedChatFlow, knowledgeMap.get(knowledge.getId()));
                } else {
                    clonedNode = factory.copyNode(originalNode, clonedChatFlow);
                }
            } else {
                clonedNode = factory.copyNode(originalNode, clonedChatFlow);
            }

            // 복제된 노드를 DB에 저장 후 맵에 추가한다.
            nodeRepository.save(clonedNode);
            nodeMap.put(originalNode.getId(), clonedNode);
        }

        // 복제된 노드들 중 Node ID를 포함한 텍스트를 속성으로 가진 노드가 있다면 전부 교체해준다.
        for (Node clonedNode : nodeMap.values()) {
            if (clonedNode.getType() == NodeType.LLM) {
                // Llm 노드라면 프롬프트 내의 변수에 복제된 노드의 ID를 새로 매핑한다.
                LLM clonedLLM = (LLM) clonedNode;
                clonedLLM.updatePrompt(
                        messageParseUtil.replace(clonedLLM.getPromptSystem(), nodeMap),
                        messageParseUtil.replace(clonedLLM.getPromptUser(), nodeMap)
                );
            } else if (clonedNode.getType() == NodeType.ANSWER) {
                // Answer 노드라면 Output Message 내의 변수에 복제된 노드의 ID를 새로 매핑한다.
                Answer clonedAnswer = (Answer) clonedNode;
                clonedAnswer.updateOutputMessage(
                        messageParseUtil.replace(clonedAnswer.getOutputMessage(), nodeMap)
                );
            }
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
            questionClassMap.put(originalQuestionClass.getId(), clonedQuestionClass);
        }

        // 간선을 복제하여 저장한다.
        List<Edge> edges = edgeRepository.findByChatFlowId(chatFlowId);

        for (Edge originalEdge : edges) {
            Long sourceNodeId = originalEdge.getSourceNode().getId();
            Long targetNodeId = originalEdge.getTargetNode().getId();
            Long sourceConditionId = originalEdge.getSourceConditionId();

            Edge edge = Edge.create(
                    nodeMap.get(sourceNodeId),
                    nodeMap.get(targetNodeId),
                    sourceConditionId == 0 ? 0 : questionClassMap.get(sourceConditionId).getId()
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

    public PreCheckResponse precheck(Long chatFlowId) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId).orElseThrow(() ->
                new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND)
        );

        List<Node> nodes = nodeRepository.findByChatFlowId(chatFlowId);
        List<Edge> edges = edgeRepository.findByChatFlowId(chatFlowId);

        Start startNode = (Start) nodes.stream()
                .filter(node -> node.getType().equals(NodeType.START))
                .findFirst()
                .orElse(null);

        if (startNode == null) {
            return PreCheckResponse.createFalse("시작 노드가 존재하지 않습니다.");
        }

        return PreCheckResponse.createTrue();
    }
}
