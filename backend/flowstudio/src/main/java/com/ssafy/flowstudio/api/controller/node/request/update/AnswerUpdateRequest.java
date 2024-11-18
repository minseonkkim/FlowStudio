package com.ssafy.flowstudio.api.controller.node.request.update;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.service.node.request.update.AnswerUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.QuestionClassifierUpdateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerUpdateRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "좌표를 입력해주세요.")
    private CoordinateRequest coordinate;
    @NotNull(message = "outputMessage를 입력해주세요.")
    private String outputMessage;

    @Builder
    private AnswerUpdateRequest(String name, CoordinateRequest coordinate, String outputMessage) {
        this.name = name;
        this.coordinate = coordinate;
        this.outputMessage = outputMessage;
    }

    public AnswerUpdateServiceRequest toServiceRequest() {
        return AnswerUpdateServiceRequest.builder()
                .name(name)
                .coordinate(coordinate.toServiceRequest())
                .outputMessage(outputMessage)
                .build();
    }
}
