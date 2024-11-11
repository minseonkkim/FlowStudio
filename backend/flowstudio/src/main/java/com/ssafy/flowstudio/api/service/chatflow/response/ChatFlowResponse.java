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
    private final List<EdgeResponse> edges;

    @Builder
    private ChatFlowResponse(Long chatFlowId, String title, List<NodeResponse> nodes, List<EdgeResponse> edges) {
        this.chatFlowId = chatFlowId;
        this.title = title;
        this.nodes = nodes;
        this.edges = edges;
    }

    public static ChatFlowResponse from(ChatFlow chatFlow, List<EdgeResponse> edges) {
        return ChatFlowResponse.builder()
                .chatFlowId(chatFlow.getId())
                .title(chatFlow.getTitle())
                .nodes(chatFlow.getNodes().stream()
                        .map(node -> {
                            System.out.println("node in DTO = " + node.getId());
                            NodeResponseFactory factory = NodeResponseFactoryProvider.getFactory(node.getType());

                            return factory.createNodeResponse(node);
                        })
                        .toList())
                .edges(edges)
                .build();
    }

}
