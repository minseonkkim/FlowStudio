package com.ssafy.flowstudio.api.service.rag.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class KnowledgeCreateServiceRequest {
    private final MultipartFile file;
    private final int chunkSize;
    private final int chunkOverlap;
    private final String separators;

    @Builder
    public KnowledgeCreateServiceRequest(MultipartFile file, int chunkSize, int chunkOverlap, String separators) {
        this.file = file;
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.separators = separators;
    }
}
