package com.ssafy.flowstudio.api.service.chat.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageServiceRequest {

    private final Long chatId;
    private final String message;

    @Builder
    private ChatMessageServiceRequest(Long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }

}
