package com.ssafy.flowstudio.api.service.node.event;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.Node;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NodeEvent extends ApplicationEvent {
    private final Node targetNode;
    private final Chat chat;

    @Builder
    private NodeEvent(Object source, Node targetNode, Chat chat) {
        super(source);
        this.targetNode = targetNode;
        this.chat = chat;
    }

    public static NodeEvent of(Object source, Node targetNode, Chat chat) {
        return NodeEvent.builder()
                .source(source)
                .targetNode(targetNode)
                .chat(chat)
                .build();
    }

}
