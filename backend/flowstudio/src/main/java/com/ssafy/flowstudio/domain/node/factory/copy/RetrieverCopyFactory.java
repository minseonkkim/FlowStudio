package com.ssafy.flowstudio.domain.node.factory.copy;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;

public class RetrieverCopyFactory extends NodeCopyFactory {
    @Override
    public Retriever copyNode(Node node, ChatFlow clonedChatFlow) {
        Retriever originalRetriever = (Retriever) node;
        return Retriever.builder()
                .chatFlow(clonedChatFlow)
                .name(originalRetriever.getName())
                .type(NodeType.RETRIEVER)
                .coordinate(
                        Coordinate.builder().x(originalRetriever.getCoordinate().getX())
                                .y(originalRetriever.getCoordinate().getY())
                                .build()
                )
                .intervalTime(originalRetriever.getIntervalTime())
                .topK(originalRetriever.getTopK())
                .scoreThreshold(originalRetriever.getScoreThreshold())
                .query(originalRetriever.getQuery())
                .build();
    }

    @Override
    public Node copyNode(Node node, ChatFlow clonedChatFlow, Knowledge knowledge) {
        Retriever retriever = copyNode(node, clonedChatFlow);
        System.out.println("updated knowledge in copynode = " + knowledge.getId());
        retriever.updateKnowledge(knowledge);
        return retriever;
    }
}
