package com.ssafy.flowstudio.api.service.chat.response;


import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatListResponse {

    private final Long id;
    private final String title;
    private final String thumbnail;
    private final List<ChatSimpleResponse> chats;

    @Builder
    private ChatListResponse(Long id, String title, String thumbnail, List<ChatSimpleResponse> chats) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.chats = chats;
    }

    public static ChatListResponse of(ChatFlow chatFlow, List<Chat> chats) {
        return ChatListResponse.builder()
                .id(chatFlow.getId())
                .title(chatFlow.getTitle())
                .thumbnail(chatFlow.getThumbnail())
                .chats(chats.stream()
                        .map(ChatSimpleResponse::of)
                        .toList())
                .build();
    }

}
