package com.ssafy.flowstudio.api.service.node.request.update;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LlmUpdateServiceRequest {
    private final String name;
    private final CoordinateServiceRequest coordinate;
    private String promptSystem;
    private String promptUser;
    private String context;
    private Double temperature;
    private Integer maxTokens;

    @Builder
    private LlmUpdateServiceRequest(String name, CoordinateServiceRequest coordinate, String promptSystem, String promptUser, String context, Double temperature, Integer maxTokens) {
        this.name = name;
        this.coordinate = coordinate;
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }
}
