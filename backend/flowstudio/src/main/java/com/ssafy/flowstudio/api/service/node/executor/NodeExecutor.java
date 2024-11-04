package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public abstract class NodeExecutor {

    protected final RedisTemplate<String, Object> redisTemplate;

    public abstract void execute(Node node);
    public abstract NodeType getNodeType();
}
