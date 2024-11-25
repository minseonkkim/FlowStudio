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

    @OneToMany(mappedBy = "questionClassifier", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionClass> questionClasses = new ArrayList<>();

    @Builder
    private QuestionClassifier(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate) {
        super(id, chatFlow, name, type, coordinate);
    }

    public static QuestionClassifier create(ChatFlow chatFlow, Coordinate coordinate) {
        return QuestionClassifier.builder()
            .chatFlow(chatFlow)
            .name("질문 분류기")
            .type(NodeType.QUESTION_CLASSIFIER)
            .coordinate(coordinate)
            .build();
    }

    @Override
    public void accept(NodeVisitor visitor, Chat chat) {
        visitor.visit(this, chat);
    }

    @Override
    public boolean hasRequiredResources() {
        if (getQuestionClasses().size() < 2) {
            return false;
        }

        if (!getQuestionClasses().stream().filter(questionClass -> questionClass.getContent() == null
                || questionClass.getContent().trim().isEmpty()).toList().isEmpty()) {
            return false;
        }

        return true;
    }

    public void addQuestionClass(QuestionClass questionClass) {
        getQuestionClasses().add(questionClass);
    }

    public void update(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }
}
