package com.ssafy.flowstudio.api.service.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TokenUsageLogResponse {

    private int tokenUsage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    private TokenUsageLogResponse(int tokenUsage, LocalDate date) {
        this.tokenUsage = tokenUsage;
        this.date = date;
    }

    public static TokenUsageLogResponse from(TokenUsageLog log) {
        return TokenUsageLogResponse.builder()
                .tokenUsage(log.getTokenUsage())
                .date(log.getCreatedAt().toLocalDate())
                .build();
    }

}
