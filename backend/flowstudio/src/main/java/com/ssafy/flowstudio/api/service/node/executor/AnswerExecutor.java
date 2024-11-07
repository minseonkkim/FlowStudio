package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.common.util.MessageParseUtil;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
public class AnswerExecutor extends NodeExecutor {

    private final MessageParseUtil messageParseUtil;
    private final SseEmitters sseEmitters;

    public AnswerExecutor(RedisService redisService, ApplicationEventPublisher eventPublisher, MessageParseUtil messageParseUtil, SseEmitters sseEmitters) {
        super(redisService, eventPublisher);
        this.messageParseUtil = messageParseUtil;
        this.sseEmitters = sseEmitters;
    }

    @Override
    public void execute(Node node, Chat chat) {
        Answer answerNode = (Answer) node;
        String outputMessage = answerNode.getOutputMessage();
        String parsedOutputMessage = messageParseUtil.replace(outputMessage, chat.getId());
        sseEmitters.send(chat.getUser(), answerNode, parsedOutputMessage);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ANSWER;
    }
}
