package com.ssafy.flowstudio.api.service.node.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CoordinateServiceRequest {

    private final int x;
    private final int y;

    @Builder
    private CoordinateServiceRequest(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
