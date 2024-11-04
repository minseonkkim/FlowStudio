package com.ssafy.flowstudio.api.controller.request;

import com.ssafy.flowstudio.api.service.chat.request.ChatMessageServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {

    @NotNull(message = "채팅 ID는 필수입니다.")
    private Long chatId;
    @NotNull(message = "메시지는 필수입니다.")
    private String message;

    @Builder
    private ChatMessageRequest(Long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }

    public ChatMessageServiceRequest toServiceRequest() {
        return ChatMessageServiceRequest.builder()
                .chatId(chatId)
                .message(message)
                .build();
    }

}
