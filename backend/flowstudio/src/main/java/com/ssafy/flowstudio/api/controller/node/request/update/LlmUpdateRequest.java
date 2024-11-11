package com.ssafy.flowstudio.api.controller.node.request.update;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.service.node.request.update.LlmUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.QuestionClassifierUpdateServiceRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LlmUpdateRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "좌표를 입력해주세요.")
    private CoordinateRequest coordinate;
    @NotNull(message = "promptSystem을 입력해주세요.")
    private String promptSystem;
    @NotNull(message = "promptUser을 입력해주세요.")
    private String promptUser;
    @NotNull(message = "context를 입력해주세요.")
    private String context;
    @NotNull(message = "temperature를 입력해주세요.")
    private Double temperature;
    @NotNull(message = "maxTokens를 입력해주세요.")
    private Integer maxTokens;

    @Builder
    private LlmUpdateRequest(String name, CoordinateRequest coordinate, String promptSystem, String promptUser, String context, Double temperature, Integer maxTokens) {
        this.name = name;
        this.coordinate = coordinate;
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    public LlmUpdateServiceRequest toServiceRequest() {
        return LlmUpdateServiceRequest.builder()
                .name(name)
                .coordinate(coordinate.toServiceRequest())
                .promptSystem(promptSystem)
                .promptUser(promptUser)
                .context(context)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .build();
    }
}
