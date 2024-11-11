package com.ssafy.flowstudio.api.service.node.request.update;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionClassifierUpdateServiceRequest {
    private final String name;
    private final CoordinateServiceRequest coordinate;

    @Builder
    private QuestionClassifierUpdateServiceRequest(String name, CoordinateServiceRequest coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }
}
