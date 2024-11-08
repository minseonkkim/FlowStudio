package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import org.springframework.stereotype.Component;


@Component
public class AnswerExecutor extends NodeExecutor {

    public AnswerExecutor(RedisService redisService) {
        super(redisService);
    }

    @Override
    public void execute(Node node, Chat chat) {
        System.out.println("AnswerExecutor");
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ANSWER;
    }
}
