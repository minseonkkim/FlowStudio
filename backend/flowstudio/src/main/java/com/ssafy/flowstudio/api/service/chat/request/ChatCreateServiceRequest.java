package com.ssafy.flowstudio.api.service.chat.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatCreateServiceRequest {
    private final boolean isPreview;

    @Builder
    private ChatCreateServiceRequest(boolean isPreview) {
        this.isPreview = isPreview;
    }
}
