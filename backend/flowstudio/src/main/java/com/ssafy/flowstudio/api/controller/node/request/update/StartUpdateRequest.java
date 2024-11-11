package com.ssafy.flowstudio.api.controller.node.request.update;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.service.node.request.update.StartUpdateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StartUpdateRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "좌표를 입력해주세요.")
    private CoordinateRequest coordinate;
    @NotNull(message = "최대 길이를 입력해주세요.")
    private Long maxLength;

    @Builder
    private StartUpdateRequest(String name, Long maxLength, CoordinateRequest coordinate) {
        this.name = name;
        this.maxLength = maxLength;
        this.coordinate = coordinate;
    }

    public StartUpdateServiceRequest toServiceRequest() {
        return StartUpdateServiceRequest.builder()
                .name(name)
                .maxLength(maxLength)
                .coordinate(coordinate.toServiceRequest())
                .build();
    }

}
