package com.ssafy.flowstudio.api.service.chatflowtest.response;

import lombok.Getter;

@Getter
public class ChatFlowTestResponse {

    private final Float embeddingDistance;
    private final Float crossEncoder;
    private final Float rougeMetric;

    public ChatFlowTestResponse(Float embeddingDistance, Float crossEncoder, Float rougeMetric) {
        this.embeddingDistance = embeddingDistance;
        this.crossEncoder = crossEncoder;
        this.rougeMetric = rougeMetric;
    }
}
