package com.ssafy.flowstudio.api.service.node.request.update;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import com.ssafy.flowstudio.domain.node.entity.ModelName;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LlmUpdateServiceRequest {
    private final String name;
    private final CoordinateServiceRequest coordinate;
    private final String promptSystem;
    private final String promptUser;
    private final String context;
    private final Double temperature;
    private final Integer maxTokens;
    private final ModelName modelName;

    @Builder
    private LlmUpdateServiceRequest(String name, CoordinateServiceRequest coordinate, String promptSystem, String promptUser, String context, Double temperature, Integer maxTokens, ModelName modelName) {
        this.name = name;
        this.coordinate = coordinate;
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.modelName = modelName;
    }

}
