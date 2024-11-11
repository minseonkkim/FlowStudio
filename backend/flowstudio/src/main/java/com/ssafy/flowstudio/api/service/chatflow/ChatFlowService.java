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
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.Start;
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

}
