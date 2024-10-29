package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.node.entity.*;

public class LlmFactory extends NodeFactory {
    @Override
    public Node createNode(Coordinate coordinate) {
        return LLM.create(coordinate);
    }
}
