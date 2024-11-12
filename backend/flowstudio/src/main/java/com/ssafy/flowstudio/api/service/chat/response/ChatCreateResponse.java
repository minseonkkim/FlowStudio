package com.ssafy.flowstudio.api.service.chat.response;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ChatCreateResponse {

    private final Long id;
    private final String title;
    private final boolean isPreview;

    @Builder
    private ChatCreateResponse(Long id, String title, boolean isPreview) {
        this.id = id;
        this.title = title;
        this.isPreview = isPreview;
    }

    public static ChatCreateResponse from(Chat chat) {
        return ChatCreateResponse.builder()
                .id(chat.getId())
                .title(chat.getTitle())
                .isPreview(chat.isPreview())
                .build();
    }

}
