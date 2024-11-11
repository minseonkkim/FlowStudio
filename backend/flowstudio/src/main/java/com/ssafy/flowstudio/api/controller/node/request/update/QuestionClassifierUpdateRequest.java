package com.ssafy.flowstudio.api.controller.node.request.update;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.service.node.request.update.QuestionClassifierUpdateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionClassifierUpdateRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "좌표를 입력해주세요.")
    private CoordinateRequest coordinate;

    @Builder
    private QuestionClassifierUpdateRequest(String name, CoordinateRequest coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public QuestionClassifierUpdateServiceRequest toServiceRequest() {
        return QuestionClassifierUpdateServiceRequest.builder()
                .name(name)
                .coordinate(coordinate.toServiceRequest())
                .build();
    }
}
