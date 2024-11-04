package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import org.springframework.stereotype.Component;

@Component
public class QuestionClassifierExecutor extends NodeExecutor {

    public QuestionClassifierExecutor(RedisService redisService) {
        super(redisService);
    }

    @Override
    public void execute(Node node) {
        System.out.println("QuestionClassifierExecutor");
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.QUESTION_CLASSIFIER;
    }
}
