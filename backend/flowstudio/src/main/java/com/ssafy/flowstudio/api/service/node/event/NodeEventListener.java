package com.ssafy.flowstudio.api.service.node.event;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeVisitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NodeEventListener {

    private final NodeVisitor visitor;

    @EventListener
    public void handleNodeEvent(NodeEvent event) {
        Node targetNode = event.getTargetNode();
        Chat chat = event.getChat();

        log.info("노드 실행 타입: {}", targetNode.getType());
        targetNode.accept(visitor, chat);
    }

}