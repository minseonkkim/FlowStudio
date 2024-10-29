package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.node.entity.*;

public class ConditionalFactory extends NodeFactory {
    @Override
    public Node createNode(Coordinate coordinate) {
        return Conditional.create(coordinate);
    }
}
