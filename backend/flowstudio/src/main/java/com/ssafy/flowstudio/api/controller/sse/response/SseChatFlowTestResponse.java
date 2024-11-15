package com.ssafy.flowstudio.api.controller.sse.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SseChatFlowTestResponse {

    private final Float embeddingDistanceMean;
    private final Float embeddingDistanceVariance;
    private final Float crossEncoderMean;
    private final Float crossEncoderVariance;
    private final Float rougeMetricMean;
    private final Float rougeMetricVariance;

    @Builder
    private SseChatFlowTestResponse(Float embeddingDistanceMean, Float embeddingDistanceVariance, Float crossEncoderMean, Float crossEncoderVariance, Float rougeMetricMean, Float rougeMetricVariance) {
        this.embeddingDistanceMean = embeddingDistanceMean;
        this.embeddingDistanceVariance = embeddingDistanceVariance;
        this.crossEncoderMean = crossEncoderMean;
        this.crossEncoderVariance = crossEncoderVariance;
        this.rougeMetricMean = rougeMetricMean;
        this.rougeMetricVariance = rougeMetricVariance;
    }

    public static SseChatFlowTestResponse of(List<Float> results) {
        return SseChatFlowTestResponse.builder()
                .embeddingDistanceMean(results.get(0))
                .embeddingDistanceVariance(results.get(1))
                .crossEncoderMean(results.get(2))
                .crossEncoderVariance(results.get(3))
                .rougeMetricMean(results.get(4))
                .rougeMetricVariance(results.get(5))
                .build();
    }

}
