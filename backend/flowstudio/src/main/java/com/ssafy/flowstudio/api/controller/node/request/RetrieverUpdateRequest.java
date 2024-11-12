package com.ssafy.flowstudio.api.controller.node.request;

import com.ssafy.flowstudio.api.service.node.request.RetrieverUpdateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RetrieverUpdateRequest {
    private final Long nodeId;
    private final Long knowledgeId;
    private final Integer intervalTime;
    private final Integer topK;
    private final Float scoreThreshold;
    private final String query;

    @Builder
    private RetrieverUpdateRequest(Long nodeId, Long knowledgeId, Integer intervalTime, Integer topK, Float scoreThreshold, String query) {
        this.nodeId = nodeId;
        this.knowledgeId = knowledgeId;
        this.intervalTime = intervalTime;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
        this.query = query;
    }

    public RetrieverUpdateServiceRequest toServiceRequest() {
        return RetrieverUpdateServiceRequest.builder()
                .nodeId(nodeId)
                .knowledgeId(knowledgeId)
                .intervalTime(intervalTime)
                .topK(topK)
                .scoreThreshold(scoreThreshold)
                .query(query)
                .build();
    }
}
