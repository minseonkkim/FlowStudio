package com.ssafy.flowstudio.api.controller.node.request.update;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import jakarta.validation.constraints.NotNull;

public class NodeUpdateRequest {
    @NotNull(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "좌표를 입력해주세요.")
    private CoordinateRequest coordinate;

    protected NodeUpdateRequest(String name, CoordinateRequest coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }
}
