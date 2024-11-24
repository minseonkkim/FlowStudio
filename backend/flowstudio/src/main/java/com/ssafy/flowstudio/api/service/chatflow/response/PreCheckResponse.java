package com.ssafy.flowstudio.api.service.chatflow.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PreCheckResponse {
    private final boolean isExecutable;
    private final String malfunctionCause;

    @Builder
    public PreCheckResponse(boolean isExecutable, String malfunctionCause) {
        this.isExecutable = isExecutable;
        this.malfunctionCause = malfunctionCause;
    }
}
