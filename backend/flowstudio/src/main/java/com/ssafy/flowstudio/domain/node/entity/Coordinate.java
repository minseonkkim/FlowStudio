package com.ssafy.flowstudio.domain.node.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Coordinate {
    private float x;
    private float y;

    @Builder
    private Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate create(float x, float y) {
        return Coordinate.builder()
            .x(x)
            .y(y)
            .build();
    }
}
