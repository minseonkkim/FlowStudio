package com.ssafy.flowstudio.api.service.node.request.update;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StartUpdateServiceRequest {
    private final String name;
    private final Long maxLength;
    private final CoordinateServiceRequest coordinate;

    @Builder
    private StartUpdateServiceRequest(String name, Long maxLength, CoordinateServiceRequest coordinate) {
        this.name = name;
        this.maxLength = maxLength;
        this.coordinate = coordinate;
    }
}
