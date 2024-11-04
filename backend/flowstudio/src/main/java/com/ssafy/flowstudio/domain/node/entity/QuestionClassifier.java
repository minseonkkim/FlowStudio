package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.model.Model;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuestionClassifier extends Node {

    @Lob
    private String classList;

    @Lob
    private String modelParamList;

    @Builder
    private QuestionClassifier(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String classList, String modelParamList) {
        super(id, chatFlow, name, type, coordinate);
        this.classList = classList;
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
}
