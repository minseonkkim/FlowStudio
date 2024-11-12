package com.ssafy.flowstudio.domain.node.factory.create;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;

public abstract class NodeFactory {
    public abstract Node createNode(ChatFlow chatFlow, Coordinate coordinate);
}
