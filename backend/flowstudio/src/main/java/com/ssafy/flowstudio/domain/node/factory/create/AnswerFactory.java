package com.ssafy.flowstudio.domain.node.factory.create;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;

public class AnswerFactory extends NodeFactory {

    @Override
    public Node createNode(ChatFlow chatFlow, Coordinate coordinate) {
        return Answer.create(chatFlow, coordinate);
    }

}
