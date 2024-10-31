package com.ssafy.flowstudio.api.service.chatflow.request;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowServiceRequest {

    private final String title;
    private final String thumbnail;
    private final String description;
    private final List<Long> categoryIds;

    @Builder
    private ChatFlowServiceRequest(String title, String thumbnail, String description, List<Long> categoryIds) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.categoryIds = categoryIds;
    }

    public static ChatFlowServiceRequest from(ChatFlowRequest request) {
        return ChatFlowServiceRequest.builder()
                .title(request.getTitle())
                .thumbnail(request.getThumbnail())
                .description(request.getDescription())
                .categoryIds(request.getCategoryIds())
                .build();
    }

}
