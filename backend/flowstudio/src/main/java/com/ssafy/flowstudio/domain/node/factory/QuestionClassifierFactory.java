package com.ssafy.flowstudio.domain.node.factory;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.*;

public class QuestionClassifierFactory extends NodeFactory {

    @Override
    public Node createNode(ChatFlow chatFlow, Coordinate coordinate) {
        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, coordinate);
        QuestionClass questionClass1 = QuestionClass.empty();
        QuestionClass questionClass2 = QuestionClass.empty();
        questionClass1.updateQuestionClassifier(questionClassifier);
        questionClass2.updateQuestionClassifier(questionClassifier);
        return questionClassifier;
    }

}
