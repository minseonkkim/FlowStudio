package com.ssafy.flowstudio.api.service.user.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiKeyServiceRequest {

    private final String openAiKey;
    private final String claudeKey;
    private final String geminiKey;
    private final String clovaKey;

    @Builder
    public ApiKeyServiceRequest(String openAiKey, String claudeKey, String geminiKey, String clovaKey) {
        this.openAiKey = openAiKey;
        this.claudeKey = claudeKey;
        this.geminiKey = geminiKey;
        this.clovaKey = clovaKey;
    }
}
