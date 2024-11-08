package com.ssafy.flowstudio.api.service.rag.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChunkListResponse {
    private final int chunkCount;
    private final List<ChunkResponse> chunkList;

    @Builder
    public ChunkListResponse(int chunkCount, List<ChunkResponse> chunkList) {
        this.chunkCount = chunkCount;
        this.chunkList = chunkList;
    }
}
