package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetrieverExecutor extends NodeExecutor {

    public RetrieverExecutor(RedisService redisService) {
        super(redisService);
    }

    @Override
    public void execute(Node node) {
        System.out.println("RetrieverExecutor");
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RETRIEVER;
    }
}
