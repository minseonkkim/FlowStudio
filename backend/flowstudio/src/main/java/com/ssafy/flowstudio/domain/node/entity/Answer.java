package com.ssafy.flowstudio.domain.node.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Answer extends Node {

    @Lob
    private String output_message;

    @Builder
    private Answer(String name, NodeType type, Coordinate coordinate, String output_key, String output_message) {
        super(null, name, type, coordinate, output_key);
        this.output_message = output_message;
    }

    public static Answer create(String name, NodeType type, Coordinate coordinate, String output_key, String output_message) {
        return Answer.builder()
            .name(name)
            .type(type)
            .coordinate(coordinate)
            .output_key(output_key)
            .output_message(output_message)
            .build();
    }

}
