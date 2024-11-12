package com.ssafy.flowstudio.api.service.rag.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KnowledgeCreateServiceResponse {
    private final Boolean isComplete;
    private final Integer totalToken;

    @Builder
    public KnowledgeCreateServiceResponse(final Boolean isComplete, final Integer totalToken) {
        this.isComplete = isComplete;
        this.totalToken = totalToken;
    }
}
