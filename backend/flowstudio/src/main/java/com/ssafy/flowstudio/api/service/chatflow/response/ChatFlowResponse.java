package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.factory.NodeResponseFactory;
import com.ssafy.flowstudio.api.service.node.response.factory.NodeResponseFactoryProvider;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowResponse {

    private final Long chatFlowId;
    private final String title;
    private final List<NodeResponse> nodes;

    @Builder
    private ChatFlowResponse(Long chatFlowId, String title, List<NodeResponse> nodes) {
        this.chatFlowId = chatFlowId;
        this.title = title;
        this.nodes = nodes;
    }

    public static ChatFlowResponse from(ChatFlow chatFlow) {
        return ChatFlowResponse.builder()
                .chatFlowId(chatFlow.getId())
                .title(chatFlow.getTitle())
                .nodes(chatFlow.getNodes().stream()
                        .map(node -> {
                            NodeResponseFactory factory = NodeResponseFactoryProvider.getFactory(node.getType());

                            return factory.createNodeResponse(node);
                        })
                        .toList())
                .build();
    }

}
