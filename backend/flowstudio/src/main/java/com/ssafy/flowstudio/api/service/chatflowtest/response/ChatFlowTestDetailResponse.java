package com.ssafy.flowstudio.api.service.chatflowtest.response;

import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowTestDetailResponse {

    private final Long id;
    private final Float embeddingDistanceMean;
    private final Float embeddingDistanceVariance;
    private final Float crossEncoderMean;
    private final Float crossEncoderVariance;
    private final Float rougeMetricMean;
    private final Float rougeMetricVariance;
    private final List<ChatFlowTestCaseResponse> chatFlowTestCases;

    @Builder
    private ChatFlowTestDetailResponse(Long id, Float embeddingDistanceMean, Float embeddingDistanceVariance, Float crossEncoderMean, Float crossEncoderVariance, Float rougeMetricMean, Float rougeMetricVariance, List<ChatFlowTestCaseResponse> chatFlowTestCases) {
        this.id = id;
        this.embeddingDistanceMean = embeddingDistanceMean;
        this.embeddingDistanceVariance = embeddingDistanceVariance;
        this.crossEncoderMean = crossEncoderMean;
        this.crossEncoderVariance = crossEncoderVariance;
        this.rougeMetricMean = rougeMetricMean;
        this.rougeMetricVariance = rougeMetricVariance;
        this.chatFlowTestCases = chatFlowTestCases;
    }

    public static ChatFlowTestDetailResponse from(ChatFlowTest chatFlowTest) {
        return ChatFlowTestDetailResponse.builder()
                .id(chatFlowTest.getId())
                .embeddingDistanceMean(chatFlowTest.getEmbeddingDistanceMean())
                .embeddingDistanceVariance(chatFlowTest.getEmbeddingDistanceVariance())
                .crossEncoderMean(chatFlowTest.getCrossEncoderMean())
                .crossEncoderVariance(chatFlowTest.getCrossEncoderVariance())
                .rougeMetricMean(chatFlowTest.getRougeMetricMean())
                .rougeMetricVariance(chatFlowTest.getRougeMetricVariance())
                .chatFlowTestCases(chatFlowTest.getChatFlowTestCases().stream()
                        .map(ChatFlowTestCaseResponse::from)
                        .toList())
                .build();
    }
}
