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
    private Conditional(Long id, String name, NodeType type, Coordinate coordinate, String output_key, String sub_conditional_list) {
        super(id, name, type, coordinate, output_key);
        this.sub_conditional_list = sub_conditional_list;
    }

    public static Conditional create(Coordinate coordinate, String output_key) {
        return Conditional.builder()
            .name("Conditional")
            .type(NodeType.CONDITIONAL)
            .coordinate(coordinate)
            .output_key(output_key)
            .build();
    }
}
