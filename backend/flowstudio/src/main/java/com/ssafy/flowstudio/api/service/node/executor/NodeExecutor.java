package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.api.service.node.event.NodeEvent;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public abstract class NodeExecutor {

    protected final RedisService redisService;
    private final ApplicationEventPublisher eventPublisher;
    protected final SseEmitters sseEmitters;

    public abstract void execute(Node node, Chat chat);
    public abstract NodeType getNodeType();

    protected void publishEvent(NodeEvent event) {
        eventPublisher.publishEvent(event);
    }

}
