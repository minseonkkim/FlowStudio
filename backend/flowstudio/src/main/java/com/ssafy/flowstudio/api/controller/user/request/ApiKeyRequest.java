package com.ssafy.flowstudio.api.controller.user.request;

import com.ssafy.flowstudio.api.service.user.request.ApiKeyServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiKeyRequest {

    private String openAiKey;
    private String claudeKey;
    private String geminiKey;
    private String clovaKey;

    @Builder
    public ApiKeyRequest(String openAiKey, String claudeKey, String geminiKey, String clovaKey) {
        this.openAiKey = openAiKey;
        this.claudeKey = claudeKey;
        this.geminiKey = geminiKey;
        this.clovaKey = clovaKey;
    }

    public ApiKeyServiceRequest toServiceRequest() {
        return ApiKeyServiceRequest.builder()
                .openAiKey(openAiKey)
                .claudeKey(claudeKey)
                .geminiKey(geminiKey)
                .clovaKey(clovaKey)
                .build();
    }
}
