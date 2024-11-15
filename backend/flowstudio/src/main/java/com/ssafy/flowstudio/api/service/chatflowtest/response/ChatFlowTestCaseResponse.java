package com.ssafy.flowstudio.api.service.chatflowtest.response;

import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTestCase;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatFlowTestCaseResponse {

    private final Long id;
    private final String testQuestion;
    private final String groundTruth;
    private final String prediction;
    private final Float embeddingDistance;
    private final Float crossEncoder;
    private final Float rougeMetric;

    @Builder
    private ChatFlowTestCaseResponse(Long id, String testQuestion, String groundTruth, String prediction, Float embeddingDistance, Float crossEncoder, Float rougeMetric) {
        this.id = id;
        this.testQuestion = testQuestion;
        this.groundTruth = groundTruth;
        this.prediction = prediction;
        this.embeddingDistance = embeddingDistance;
        this.crossEncoder = crossEncoder;
        this.rougeMetric = rougeMetric;
    }

    public static ChatFlowTestCaseResponse from(ChatFlowTestCase chatFlowTestCase) {
        return new ChatFlowTestCaseResponse(
                chatFlowTestCase.getId(),
                chatFlowTestCase.getQuestion(),
                chatFlowTestCase.getGroundTruth(),
                chatFlowTestCase.getPrediction(),
                chatFlowTestCase.getEmbeddingDistance(),
                chatFlowTestCase.getCrossEncoder(),
                chatFlowTestCase.getRougeMetric()
        );
    }

}
