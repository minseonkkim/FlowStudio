package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuestionClassifier extends Node {

    @Lob
    private String modelParamList;

    @OneToMany(mappedBy = "questionClassifier", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionClass> questionClasses = new ArrayList<>();

    @Builder
    private QuestionClassifier(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String modelParamList) {
        super(id, chatFlow, name, type, coordinate);
        this.modelParamList = modelParamList;
    }

    public static QuestionClassifier create(ChatFlow chatFlow, Coordinate coordinate) {
        return QuestionClassifier.builder()
            .chatFlow(chatFlow)
            .name("Question Classifier")
            .type(NodeType.QUESTION_CLASSIFIER)
            .coordinate(coordinate)
            .build();
    }

    @Override
    public void accept(NodeVisitor visitor, Chat chat) {
        visitor.visit(this, chat);
    }

    public void addQuestionClass(QuestionClass questionClass) {
        getQuestionClasses().add(questionClass);
    }
}
