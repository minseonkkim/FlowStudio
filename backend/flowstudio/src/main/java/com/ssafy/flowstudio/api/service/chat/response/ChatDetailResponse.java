package com.ssafy.flowstudio.api.service.chat.response;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatDetailResponse {
    private Long id;
    private String title;
    private String messageList;

    @Builder
    private ChatDetailResponse(Long id, String title, String messageList) {
        this.id = id;
        this.title = title;
        this.messageList = messageList;
    }

    public static ChatDetailResponse from(Chat chat) {
        return ChatDetailResponse.builder()
                .id(chat.getId())
                .title(chat.getTitle())
                .messageList(chat.getMessageList())
                .build();
    }

}
