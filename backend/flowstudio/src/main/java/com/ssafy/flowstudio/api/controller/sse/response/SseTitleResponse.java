package com.ssafy.flowstudio.api.controller.sse.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SseTitleResponse {

    private final Long chatId;
    private final String title;

    @Builder
    private SseTitleResponse(Long chatId, String title) {
        this.chatId = chatId;
        this.title = title;
    }

    public static SseTitleResponse of(Long chatId, String title) {
        return SseTitleResponse.builder()
                .chatId(chatId)
                .title(title)
                .build();
    }

}
