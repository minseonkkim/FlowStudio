package com.ssafy.flowstudio.api.service.node.event;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeVisitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NodeEventListener {

    private final NodeVisitor visitor;
    private final SseEmitters sseEmitters;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleNodeEvent(NodeEvent event) {
        Node targetNode = event.getTargetNode();
        Chat chat = event.getChat();

        log.info("노드 실행 타입: {}", targetNode.getType());
        targetNode.accept(visitor, chat);
    }

}