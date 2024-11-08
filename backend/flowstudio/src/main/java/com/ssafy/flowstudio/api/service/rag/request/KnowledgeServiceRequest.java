package com.ssafy.flowstudio.api.service.rag.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KnowledgeServiceRequest {
    private final Boolean isPublic;
    private final String title;

    @Builder
    public KnowledgeServiceRequest(Boolean isPublic, String title) {
        this.isPublic = isPublic;
        this.title = title;
    }
}