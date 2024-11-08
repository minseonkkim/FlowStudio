package com.ssafy.flowstudio.api.service.user.response;

import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiKeyResponse {
    private String openAiKey;
    private String claudeKey;
    private String geminiKey;
    private String clovaKey;

    @Builder
    private ApiKeyResponse(String openAiKey, String claudeKey, String geminiKey, String clovaKey) {
        this.openAiKey = openAiKey;
        this.claudeKey = claudeKey;
        this.geminiKey = geminiKey;
        this.clovaKey = clovaKey;
    }

    public static ApiKeyResponse from(ApiKey apiKey) {
        return ApiKeyResponse.builder()
                .openAiKey(apiKey.getOpenAiKey())
                .claudeKey(apiKey.getClaudeKey())
                .geminiKey(apiKey.getGeminiKey())
                .clovaKey(apiKey.getClovaKey())
                .build();
    }
}
