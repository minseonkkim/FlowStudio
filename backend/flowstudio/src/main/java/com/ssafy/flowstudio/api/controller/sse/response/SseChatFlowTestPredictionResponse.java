package com.ssafy.flowstudio.api.controller.sse.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SseChatFlowTestPredictionResponse {

    private final Long chatId;
    private final String prediction;

    @Builder
    private SseChatFlowTestPredictionResponse(Long chatId, String prediction) {
        this.chatId = chatId;
        this.prediction = prediction;
    }

    public static SseChatFlowTestPredictionResponse of(Long chatId, String prediction) {
        return SseChatFlowTestPredictionResponse.builder()
                .chatId(chatId)
                .prediction(prediction)
                .build();
    }

}
