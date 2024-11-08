package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowUpdateResponse {

    private final Long chatFlowId;
    private final String title;
    private final String description;
    private final String thumbnail;
    private final List<CategoryResponse> categories;

    @Builder
    private ChatFlowUpdateResponse(Long chatFlowId, String title, String description, String thumbnail, List<CategoryResponse> categories) {
        this.chatFlowId = chatFlowId;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.categories = categories;
    }

    public static ChatFlowUpdateResponse of(ChatFlow chatFlow, List<Category> categories) {
        return ChatFlowUpdateResponse.builder()
                .chatFlowId(chatFlow.getId())
                .title(chatFlow.getTitle())
                .description(chatFlow.getDescription())
                .thumbnail(chatFlow.getThumbnail())
                .categories(categories.stream()
                        .map(CategoryResponse::from)
                        .toList())
                .build();
    }

}
