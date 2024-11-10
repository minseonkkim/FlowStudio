package com.ssafy.flowstudio.api.service.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TokenUsageLogResponse {

    private Long id;
    private int tokenUsage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    private TokenUsageLogResponse(Long id, int tokenUsage, LocalDateTime createdAt) {
        this.id = id;
        this.tokenUsage = tokenUsage;
        this.createdAt = createdAt;
    }

    public static TokenUsageLogResponse from(TokenUsageLog log) {
        return TokenUsageLogResponse.builder()
                .id(log.getId())
                .tokenUsage(log.getTokenUsage())
                .createdAt(log.getCreatedAt())
                .build();
    }

}
