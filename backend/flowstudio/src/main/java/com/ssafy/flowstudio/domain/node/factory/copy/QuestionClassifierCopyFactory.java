package com.ssafy.flowstudio.domain.node.factory.copy;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;

public class QuestionClassifierCopyFactory extends NodeCopyFactory {
    @Override
    public Node copyNode(Node node, ChatFlow clonedChatFlow) {
        QuestionClassifier originalQuestionClassifier = (QuestionClassifier) node;
        return QuestionClassifier.builder()
                .chatFlow(clonedChatFlow)
                .name(originalQuestionClassifier.getName())
                .type(NodeType.QUESTION_CLASSIFIER)
                .coordinate(
                        Coordinate.builder().x(originalQuestionClassifier.getCoordinate().getX())
                                .y(originalQuestionClassifier.getCoordinate().getY())
                                .build()
                )
                .build();
    }
}
