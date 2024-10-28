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
public class Start extends Node {

    @Column
    private int maxLength;

    @Builder
    private Start(String name, NodeType type, Coordinate coordinate, String output_key, int maxLength) {
        super(null, name, type, coordinate, output_key);
        this.maxLength = maxLength;
    }

    public static Start create(String name, NodeType type, Coordinate coordinate, String output_key, int maxLength) {
        return Start.builder()
            .name(name)
            .type(type)
            .coordinate(coordinate)
            .output_key(output_key)
            .maxLength(maxLength)
            .build();
    }

}
