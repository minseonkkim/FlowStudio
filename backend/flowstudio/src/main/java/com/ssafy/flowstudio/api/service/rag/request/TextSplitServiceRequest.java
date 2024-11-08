package com.ssafy.flowstudio.api.service.rag.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class TextSplitServiceRequest {
    private final String text;
    private final int chunkSize;
    private final int chunkOverlap;
    private final String separators;

    @Builder
    public TextSplitServiceRequest(String text, int chunkSize, int chunkOverlap, String separators) {
        this.text = text;
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.separators = separators;
    }
}
