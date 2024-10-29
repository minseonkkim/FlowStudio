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
    private Start(String name, NodeType type, Coordinate coordinate, int maxLength) {
        super(null, name, type, coordinate);
        this.maxLength = maxLength;
    }

    public static Start create(Coordinate coordinate) {
        return Start.builder()
            .name("Start")
            .type(NodeType.START)
            .coordinate(coordinate)
            .build();
    }

}
