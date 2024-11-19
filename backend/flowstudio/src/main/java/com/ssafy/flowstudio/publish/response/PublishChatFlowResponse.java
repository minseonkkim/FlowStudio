package com.ssafy.flowstudio.publish.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PublishChatFlowResponse {
    private final Long chatFlowId;
    private final String publishUrl;
    private final String publishedAt;

    @Builder
    public PublishChatFlowResponse(final Long chatFlowId, final String publishUrl, final LocalDateTime publishedAt) {
        this.chatFlowId = chatFlowId;
        this.publishUrl = publishUrl;
        this.publishedAt = publishedAt != null ? publishedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}
