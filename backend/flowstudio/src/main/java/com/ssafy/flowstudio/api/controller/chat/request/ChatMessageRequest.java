package com.ssafy.flowstudio.api.controller.chat.request;

import com.ssafy.flowstudio.api.service.chat.request.ChatMessageServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {

    @NotNull(message = "메시지는 필수입니다.")
    private String message;

    @Builder
    private ChatMessageRequest(String message) {
        this.message = message;
    }

    public ChatMessageServiceRequest toServiceRequest() {
        return ChatMessageServiceRequest.builder()
                .message(message)
                .build();
    }

}
