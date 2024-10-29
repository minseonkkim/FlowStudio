package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.node.entity.*;

public class RetrieverFactory extends NodeFactory {
    @Override
    public Node createNode(Coordinate coordinate) {
        return Retriever.create(coordinate);
    }
}
