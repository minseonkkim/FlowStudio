package com.ssafy.flowstudio.api.service.chatflowtest;

import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestServiceRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowTestCreateResponse {

    private final Long chatId;
    private final String testQuestion;
    private final String groundTruth;

    @Builder
    private ChatFlowTestCreateResponse(Long chatId, String testQuestion, String groundTruth) {
        this.chatId = chatId;
        this.testQuestion = testQuestion;
        this.groundTruth = groundTruth;
    }

    public static ChatFlowTestCreateResponse from(Long chatId, ChatFlowTestServiceRequest request) {
        return ChatFlowTestCreateResponse.builder()
                .chatId(chatId)
                .testQuestion(request.getTestQuestion())
                .groundTruth(request.getGroundTruth())
                .build();
    }
}
