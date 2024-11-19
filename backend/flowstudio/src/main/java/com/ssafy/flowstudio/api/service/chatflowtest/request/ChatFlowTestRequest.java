package com.ssafy.flowstudio.api.service.chatflowtest.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatFlowTestRequest {

    private final String groundTruth;
    private final String prediction;

    @Builder
    private ChatFlowTestRequest(String groundTruth, String prediction) {
        this.groundTruth = groundTruth;
        this.prediction = prediction;
    }

    public static ChatFlowTestRequest of(String groundTruth, String prediction) {
        return ChatFlowTestRequest.builder()
                .groundTruth(groundTruth)
                .prediction(prediction)
                .build();
    }

}
