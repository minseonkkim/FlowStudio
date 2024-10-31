package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatFlowUpdateResponse {

    private final Long chatFlowId;
    private final String title;
    private final String description;
    private final String thumbnail;

    @Builder
    private ChatFlowUpdateResponse(Long chatFlowId, String title, String description, String thumbnail) {
        this.chatFlowId = chatFlowId;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static ChatFlowUpdateResponse from(ChatFlow chatFlow) {
        return ChatFlowUpdateResponse.builder()
                .chatFlowId(chatFlow.getId())
                .title(chatFlow.getTitle())
                .description(chatFlow.getDescription())
                .thumbnail(chatFlow.getThumbnail())
                .build();
    }

}
