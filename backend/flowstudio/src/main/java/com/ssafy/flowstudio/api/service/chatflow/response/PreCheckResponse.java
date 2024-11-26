package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.node.entity.Node;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PreCheckResponse {
    private final boolean isExecutable;
    private final int errorCode;
    private final String malfunctionCause;

    @Builder
    public PreCheckResponse(boolean isExecutable, int errorCode, String malfunctionCause) {
        this.isExecutable = isExecutable;
        this.errorCode = errorCode;
        this.malfunctionCause = malfunctionCause;
    }

    public static PreCheckResponse createTrue() {
        return PreCheckResponse.builder()
                .isExecutable(true)
                .build();
    }

    public static PreCheckResponse createFalse(ErrorCode errorCode) {
        return PreCheckResponse.builder()
                .isExecutable(false)
                .errorCode(errorCode.getCode())
                .malfunctionCause(errorCode.getMessage())
                .build();
    }

    public static PreCheckResponse createFalse(ErrorCode errorCode, Node node) {
        return PreCheckResponse.builder()
                .isExecutable(false)
                .errorCode(errorCode.getCode())
                .malfunctionCause("오류 발생 노드: " + node.getName() + ", " + errorCode.getMessage())
                .build();
    }
}
