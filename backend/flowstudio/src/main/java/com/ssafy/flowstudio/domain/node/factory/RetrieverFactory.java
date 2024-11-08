package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.*;

public class RetrieverFactory extends NodeFactory {

    @Override
    public Node createNode(ChatFlow chatFlow, Coordinate coordinate) {
        return Retriever.create(chatFlow, coordinate, 1, 3, 0);
    }

}
