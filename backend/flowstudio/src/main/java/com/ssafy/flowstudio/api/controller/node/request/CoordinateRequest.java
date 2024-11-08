package com.ssafy.flowstudio.api.controller.node.request;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CoordinateRequest {

    private int x;
    private int y;

    @Builder
    private CoordinateRequest(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static CoordinateServiceRequest toServiceRequest(CoordinateRequest coordinate) {
        return CoordinateServiceRequest.builder()
            .x(coordinate.getX())
            .y(coordinate.getY())
            .build();
    }

}
