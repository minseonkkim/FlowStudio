package com.ssafy.flowstudio.domain.node.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VariableAssigner extends Node {

    @Column
    private String targetVariableName;

    @Column
    private String targetVariableType;

    @Builder
    private VariableAssigner(String name, NodeType type, Coordinate coordinate, String output_key, String targetVariableName, String targetVariableType) {
        super(null, name, type, coordinate, output_key);
        this.targetVariableName = targetVariableName;
        this.targetVariableType = targetVariableType;
    }

    public static VariableAssigner create(String name, NodeType type, Coordinate coordinate, String output_key) {
        return VariableAssigner.builder()
            .name(name)
            .type(type)
            .coordinate(coordinate)
            .output_key(output_key)
            .build();
    }

}
