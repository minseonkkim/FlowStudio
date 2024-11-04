
package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class VariableAssignerExecutor extends NodeExecutor {

    public VariableAssignerExecutor(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void execute(Node node) {
        System.out.println("VariableAssignerExecutor");
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.VARIABLE_ASSIGNER;
    }
}
