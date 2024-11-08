package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.user.entity.User;

public abstract class NodeFactory {
    public abstract Node createNode(ChatFlow chatFlow, Coordinate coordinate);
}
