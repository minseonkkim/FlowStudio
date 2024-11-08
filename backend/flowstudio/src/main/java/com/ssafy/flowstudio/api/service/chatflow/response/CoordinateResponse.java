package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CoordinateResponse {

    private final float x;
    private final float y;

    @Builder
    private CoordinateResponse(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static CoordinateResponse from(Coordinate coordinate) {
        return CoordinateResponse.builder()
                .x(coordinate.getX())
                .y(coordinate.getY())
                .build();
    }

}
