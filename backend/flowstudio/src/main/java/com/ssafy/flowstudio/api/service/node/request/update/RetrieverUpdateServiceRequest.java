package com.ssafy.flowstudio.api.service.node.request.update;

import com.ssafy.flowstudio.api.service.node.request.CoordinateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RetrieverUpdateServiceRequest {

    private final String name;
    private CoordinateServiceRequest coordinate;
    private final Long knowledgeId;
    private final Integer intervalTime;
    private final Integer topK;
    private final Float scoreThreshold;
    private final String query;

    @Builder
    private RetrieverUpdateServiceRequest(String name, CoordinateServiceRequest coordinate, Long knowledgeId, Integer intervalTime, Integer topK, Float scoreThreshold, String query) {
        this.name = name;
        this.coordinate = coordinate;
        this.knowledgeId = knowledgeId;
        this.intervalTime = intervalTime;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
        this.query = query;
    }

}
