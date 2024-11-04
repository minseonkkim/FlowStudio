package com.ssafy.flowstudio.api.service.rag.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChunkResponse {
    private final String chunkId;
    private final String content;

    @Builder
    public ChunkResponse(String chunkId, String content) {
        this.chunkId = chunkId;
        this.content = content;
    }
}
