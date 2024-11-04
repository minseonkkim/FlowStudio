package com.ssafy.flowstudio.api.service.rag.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChunkResponse {
    private final Integer chunkId;
    private final String content;

    @Builder
    public ChunkResponse(Integer chunkId, String content) {
        this.chunkId = chunkId;
        this.content = content;
    }
}
