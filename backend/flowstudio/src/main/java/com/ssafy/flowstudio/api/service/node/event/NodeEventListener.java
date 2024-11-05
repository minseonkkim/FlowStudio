package com.ssafy.flowstudio.api.service.node.event;

import com.ssafy.flowstudio.api.service.node.NodeVisitorImpl;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NodeEventListener {

    private final NodeVisitorImpl visitor;

    @EventListener
    public void handleNodeEvent(NodeEvent event) {

        Node targetNode = event.getTargetNode();
        Chat chat = event.getChat();

        targetNode.accept(visitor, chat);
    }
}