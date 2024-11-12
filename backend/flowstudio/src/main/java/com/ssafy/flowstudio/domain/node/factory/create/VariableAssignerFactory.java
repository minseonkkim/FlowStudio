package com.ssafy.flowstudio.domain.node.factory.create;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.VariableAssigner;

public class VariableAssignerFactory extends NodeFactory {

    @Override
    public Node createNode(ChatFlow chatFlow, Coordinate coordinate) {
        return VariableAssigner.create(chatFlow, coordinate);
    }

}
