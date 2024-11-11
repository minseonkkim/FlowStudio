package com.ssafy.flowstudio.domain.node.factory.copy;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.factory.create.NodeFactory;

public class AnswerCopyFactory extends NodeCopyFactory {
    @Override
    public Answer copyNode(Node node, ChatFlow clonedChatFlow) {
        Answer originalAnswer = (Answer) node;
        System.out.println("Answer receiving clonedChatFlow = " + clonedChatFlow.getId());
        return Answer.builder()
                .chatFlow(clonedChatFlow)
                .name(originalAnswer.getName())
                .type(NodeType.ANSWER)
                .coordinate(
                        Coordinate.builder().x(originalAnswer.getCoordinate().getX())
                                .y(originalAnswer.getCoordinate().getY())
                                .build()
                )
                .outputMessage(originalAnswer.getOutputMessage())
                .build();
    }
}
