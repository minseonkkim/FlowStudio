package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.*;

public class QuestionClassifierFactory extends NodeFactory {

    @Override
    public Node createNode(ChatFlow chatFlow, Coordinate coordinate) {
        return QuestionClassifier.create(chatFlow, coordinate);
    }

}
