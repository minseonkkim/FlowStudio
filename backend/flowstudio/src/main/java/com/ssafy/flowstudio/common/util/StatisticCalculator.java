package com.ssafy.flowstudio.common.util;

import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTestCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticCalculator {

    public List<Float> calculate(List<ChatFlowTestCase> testCases) {
        // `embeddingDistance` 리스트 추출
        List<Float> embeddingDistances = testCases.stream()
                .map(ChatFlowTestCase::getEmbeddingDistance)
                .toList();
        // 평균 및 분산 계산
        float embeddingMean = calculateMean(embeddingDistances);
        float embeddingVariance = calculateVariance(embeddingDistances, embeddingMean);

        // `crossEncoder` 리스트 추출
        List<Float> crossEncoders = testCases.stream()
                .map(ChatFlowTestCase::getCrossEncoder)
                .toList();
        // 평균 및 분산 계산
        float crossEncoderMean = calculateMean(crossEncoders);
        float crossEncoderVariance = calculateVariance(crossEncoders, crossEncoderMean);

        // `rougeMetric` 리스트 추출
        List<Float> rougeMetrics = testCases.stream()
                .map(ChatFlowTestCase::getRougeMetric)
                .toList();

        // 평균 및 분산 계산
        float rougeMean = calculateMean(rougeMetrics);
        float rougeVariance = calculateVariance(rougeMetrics, rougeMean);

        return List.of(embeddingMean, embeddingVariance, crossEncoderMean, crossEncoderVariance, rougeMean, rougeVariance);
    }

    private float calculateMean(List<Float> values) {
        return (float) values.stream()
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0.0);
    }

    private float calculateVariance(List<Float> values, float mean) {
        long count = values.size();
        if (count <= 1) return 0.0F;

        float sumOfSquaredDeviations = (float) values.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .sum();

        return sumOfSquaredDeviations / (count - 1);
    }
}
