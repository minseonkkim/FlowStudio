package com.ssafy.flowstudio.api.service.chatflow;

import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowListResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowUpdateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlowCategory;
import com.ssafy.flowstudio.domain.chatflow.repository.CategoryRepository;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import com.ssafy.flowstudio.domain.node.entity.Start;
import com.ssafy.flowstudio.domain.node.repository.QuestionClassRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatFlowService {

    private final ChatFlowRepository chatFlowRepository;
    private final CategoryRepository categoryRepository;
    private final EdgeRepository edgeRepository;

    public List<ChatFlowListResponse> getChatFlows(User user) {
        List<ChatFlow> chatFlows = chatFlowRepository.findByUser(user);

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

        llm.updatePrompt("아래 글에 기반해서 대답해줘\n \\n\\n{{"+ retrieverId +"}}", "{{INPUT_MESSAGE}}");
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
}
