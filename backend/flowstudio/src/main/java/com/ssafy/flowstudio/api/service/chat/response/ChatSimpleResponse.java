package com.ssafy.flowstudio.api.service.chat.response;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatSimpleResponse {
    private final Long id;
    private final String title;

    @Builder
    private ChatSimpleResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ChatSimpleResponse of(Chat chat) {
        return ChatSimpleResponse.builder()
                .id(chat.getId())
                .title(chat.getTitle())
                .build();
    }
}
