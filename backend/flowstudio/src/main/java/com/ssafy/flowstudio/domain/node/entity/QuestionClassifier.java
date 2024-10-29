package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuestionClassifier extends Node {

    @Lob
    private String class_list;

    @Lob
    private String model_param_list;

    @Builder
    private QuestionClassifier(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String class_list, String model_param_list) {
        super(id, chatFlow, name, type, coordinate);
        this.class_list = class_list;
        this.model_param_list = model_param_list;
    }

    public static QuestionClassifier create(ChatFlow chatFlow, Coordinate coordinate) {
        return QuestionClassifier.builder()
            .chatFlow(chatFlow)
            .name("Question Classifier")
            .type(NodeType.QUESTION_CLASSIFIER)
            .coordinate(coordinate)
            .build();
    }
}
