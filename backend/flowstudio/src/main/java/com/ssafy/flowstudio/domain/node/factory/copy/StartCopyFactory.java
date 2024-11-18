package com.ssafy.flowstudio.domain.node.factory.copy;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Start;

public class StartCopyFactory extends NodeCopyFactory {
    public Start copyNode(Node node, ChatFlow clonedChatFlow) {
        Start originalStart = (Start) node;
        return Start.builder()
                .chatFlow(clonedChatFlow)
                .name(originalStart.getName())
                .type(NodeType.START)
                .coordinate(
                        Coordinate.builder().x(originalStart.getCoordinate().getX())
                                .y(originalStart.getCoordinate().getY())
                                .build()
                )
                .maxLength(originalStart.getMaxLength())
                .build();
    }
}
