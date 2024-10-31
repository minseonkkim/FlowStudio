package com.ssafy.flowstudio.api.service.chatflow.request;

import com.ssafy.flowstudio.api.controller.chatflow.request.ChatFlowCreateRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowCreateServiceRequest {

    private final String title;
    private final String thumbnail;
    private final String description;
    private final List<Long> categoryIds;

    @Builder
    private ChatFlowCreateServiceRequest(String title, String thumbnail, String description, List<Long> categoryIds) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.categoryIds = categoryIds;
    }

    public static ChatFlowCreateServiceRequest from(ChatFlowCreateRequest request) {
        return ChatFlowCreateServiceRequest.builder()
                .title(request.getTitle())
                .thumbnail(request.getThumbnail())
                .description(request.getDescription())
                .categoryIds(request.getCategoryIds())
                .build();
    }

}
