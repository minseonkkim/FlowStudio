package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;

public class AnswerFactory extends NodeFactory {
    @Override
    public Node createNode(Coordinate coordinate) {
        return Answer.create(coordinate);
    }
}
