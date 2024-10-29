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
public class Answer extends Node {

    @Lob
    private String output_message;

    @Builder
    private Answer(String name, NodeType type, Coordinate coordinate, String output_key) {
        super(null, name, type, coordinate);
        this.output_message = output_message;
    }

    public static Answer create(Coordinate coordinate) {
        return Answer.builder()
            .name("Answer")
            .type(NodeType.ANSWER)
            .coordinate(coordinate)
            .build();
    }

}
