package com.ssafy.flowstudio.domain.node.factory.create;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.*;

public class ConditionalFactory extends NodeFactory {

    @Override
    public Node createNode(ChatFlow chatFlow, Coordinate coordinate) {
        return Conditional.create(chatFlow, coordinate);
    }

}
