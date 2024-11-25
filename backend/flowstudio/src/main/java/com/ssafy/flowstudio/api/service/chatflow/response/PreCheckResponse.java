package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;
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

    public static PreCheckResponse createTrue() {
        return PreCheckResponse.builder()
                .isExecutable(true)
                .build();
    }

    public static PreCheckResponse createFalse(String malfunctionCause) {
        return PreCheckResponse.builder()
                .isExecutable(false)
                .malfunctionCause(malfunctionCause)
                .build();
    }
}
