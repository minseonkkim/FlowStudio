package com.ssafy.flowstudio.api.service.chat.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageServiceRequest {

    private final String message;

    @Builder
    private ChatMessageServiceRequest(String message) {
        this.message = message;
    }

    public static ChatMessageServiceRequest from(String testQuestion) {
        return ChatMessageServiceRequest.builder()
                .message(testQuestion)
                .build();
    }

}
