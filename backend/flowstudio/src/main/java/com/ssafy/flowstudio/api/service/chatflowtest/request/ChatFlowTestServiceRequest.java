package com.ssafy.flowstudio.api.service.chatflowtest.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatFlowTestServiceRequest {

    private final String testQuestion;
    private final String groundTruth;

    @Builder
    private ChatFlowTestServiceRequest(String testQuestion, String groundTruth) {
        this.testQuestion = testQuestion;
        this.groundTruth = groundTruth;
    }

}
