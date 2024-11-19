package com.ssafy.flowstudio.api.controller.chatflowtest.request;

import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowTestRequest {

    @NotBlank(message = "테스트 질문은 필수입니다.")
    private String testQuestion;
    @NotBlank(message = "정답은 필수입니다.")
    private String groundTruth;

    @Builder
    private ChatFlowTestRequest(String testQuestion, String groundTruth) {
        this.testQuestion = testQuestion;
        this.groundTruth = groundTruth;
    }

    public ChatFlowTestServiceRequest toServiceRequest() {
        return ChatFlowTestServiceRequest.builder()
                .testQuestion(testQuestion)
                .groundTruth(groundTruth)
                .build();
    }
}
