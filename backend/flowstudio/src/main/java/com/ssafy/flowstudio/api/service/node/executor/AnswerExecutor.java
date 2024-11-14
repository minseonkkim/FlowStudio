package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.api.service.chatflowtest.event.ChatFlowTestEvent;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
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

    public AnswerExecutor(RedisService redisService, ApplicationEventPublisher eventPublisher, MessageParseUtil messageParseUtil, SseEmitters sseEmitters) {
        super(redisService, eventPublisher, sseEmitters);
        this.messageParseUtil = messageParseUtil;
    }

    @Override
    public void execute(Node node, Chat chat) {
        Answer answerNode = (Answer) node;

        // 사용자가 변수와 함께 등록한 Output Message를 파싱한다.
        String outputMessage = answerNode.getOutputMessage();

        // Answer 노드는 Output Message를 필수로 필요로 한다.
        if (outputMessage == null) {
            throw new BaseException(ErrorCode.NODE_VALUE_NOT_EXIST);
        }

        String parsedOutputMessage = messageParseUtil.replace(outputMessage, chat.getId());

        // 완성된 메시지를 SSE를 통해 클라이언트에게 전송한다.
        sseEmitters.send(chat.getUser(), answerNode, parsedOutputMessage);

        if(chat.isTest()) {
            ChatFlowTestEvent event = ChatFlowTestEvent.of(this, chat);
            publishEvent(event);
        }
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ANSWER;
    }
}
