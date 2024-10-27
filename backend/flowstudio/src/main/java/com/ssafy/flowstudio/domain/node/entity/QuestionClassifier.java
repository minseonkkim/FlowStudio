package com.ssafy.flowstudio.domain.node.entity;

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
    private QuestionClassifier(Long id, String name, NodeType type, Coordinate coordinate, String output_key, String class_list, String model_param_list) {
        super(id, name, type, coordinate, output_key);
        this.class_list = class_list;
        this.model_param_list = model_param_list;
    }

    public static QuestionClassifier create(Coordinate coordinate, String output_key) {
        return QuestionClassifier.builder()
            .name("Question Classifier")
            .type(NodeType.QUESTION_CLASSIFIER)
            .coordinate(coordinate)
            .output_key(output_key)
            .build();
    }
}
