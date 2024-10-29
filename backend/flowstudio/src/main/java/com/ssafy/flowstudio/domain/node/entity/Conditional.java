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
public class Conditional extends Node {

    @Lob
    private String sub_conditional_list;

    @Builder
    private Conditional(Long id, String name, NodeType type, Coordinate coordinate, String sub_conditional_list) {
        super(id, name, type, coordinate);
        this.sub_conditional_list = sub_conditional_list;
    }

    public static Conditional create(Coordinate coordinate) {
        return Conditional.builder()
            .name("Conditional")
            .type(NodeType.CONDITIONAL)
            .coordinate(coordinate)
            .build();
    }
}
