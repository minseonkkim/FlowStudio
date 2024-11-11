package com.ssafy.flowstudio.api.service.node.request.update;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AnswerUpdateServiceRequest {

    private final String name;
    private final CoordinateServiceRequest coordinate;
    private final String outputMessage;

    @Builder
    private AnswerUpdateServiceRequest(String name, CoordinateServiceRequest coordinate, String outputMessage) {
        this.name = name;
        this.coordinate = coordinate;
        this.outputMessage = outputMessage;
    }
}
