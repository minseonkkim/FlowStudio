package com.ssafy.flowstudio.api.service.node.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RetrieverUpdateServiceRequest {
    private final Long nodeId;
    private final Long knowledgeId;
    private final Integer intervalTime;
    private final Integer topK;
    private final Float scoreThreshold;
    private final String query;

    @Builder
    private RetrieverUpdateServiceRequest(Long nodeId, Long knowledgeId, Integer intervalTime, Integer topK, Float scoreThreshold, String query) {
        this.nodeId = nodeId;
        this.knowledgeId = knowledgeId;
        this.intervalTime = intervalTime;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
        this.query = query;
    }
}
