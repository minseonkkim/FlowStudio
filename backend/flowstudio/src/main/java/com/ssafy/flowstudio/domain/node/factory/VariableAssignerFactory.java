package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.VariableAssigner;

public class VariableAssignerFactory extends NodeFactory {
    @Override
    public Node createNode(Coordinate coordinate) {
        return VariableAssigner.create(coordinate);
    }
}
