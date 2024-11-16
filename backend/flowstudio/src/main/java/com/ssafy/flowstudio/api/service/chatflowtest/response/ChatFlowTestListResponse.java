package com.ssafy.flowstudio.api.service.chatflowtest.response;


import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatFlowTestListResponse {

    private final Long id;
    private final Float embeddingDistanceMean;
    private final Float embeddingDistanceVariance;
    private final Float crossEncoderMean;
    private final Float crossEncoderVariance;
    private final Float rougeMetricMean;
    private final Float rougeMetricVariance;
    private final Integer totalTestCount;

    @Builder
    private ChatFlowTestListResponse(Long id, Float embeddingDistanceMean, Float embeddingDistanceVariance, Float crossEncoderMean, Float crossEncoderVariance, Float rougeMetricMean, Float rougeMetricVariance, Integer totalTestCount) {
        this.id = id;
        this.embeddingDistanceMean = embeddingDistanceMean;
        this.embeddingDistanceVariance = embeddingDistanceVariance;
        this.crossEncoderMean = crossEncoderMean;
        this.crossEncoderVariance = crossEncoderVariance;
        this.rougeMetricMean = rougeMetricMean;
        this.rougeMetricVariance = rougeMetricVariance;
        this.totalTestCount = totalTestCount;
    }

    public static ChatFlowTestListResponse from(ChatFlowTest chatFlowTest) {
        return ChatFlowTestListResponse.builder()
                .id(chatFlowTest.getId())
                .embeddingDistanceMean(chatFlowTest.getEmbeddingDistanceMean())
                .embeddingDistanceVariance(chatFlowTest.getEmbeddingDistanceVariance())
                .crossEncoderMean(chatFlowTest.getCrossEncoderMean())
                .crossEncoderVariance(chatFlowTest.getCrossEncoderVariance())
                .rougeMetricMean(chatFlowTest.getRougeMetricMean())
                .rougeMetricVariance(chatFlowTest.getRougeMetricVariance())
                .totalTestCount(chatFlowTest.getTotalTestCount())
                .build();
    }

}
