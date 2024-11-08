package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFlowListResponse {

    private final Long chatFlowId;
    private final String title;
    private final String description;
    private final UserResponse author;
    private final String thumbnail;
    private final List<CategoryResponse> categories;
    private final boolean isPublic;

    @Builder
    private ChatFlowListResponse(Long chatFlowId, String title, String description, UserResponse author, String thumbnail, List<CategoryResponse> categories, boolean isPublic) {
        this.chatFlowId = chatFlowId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.thumbnail = thumbnail;
        this.categories = categories;
        this.isPublic = isPublic;
    }

    public static ChatFlowListResponse of(ChatFlow chatFlow, List<Category> categories) {
        return ChatFlowListResponse.builder()
                .chatFlowId(chatFlow.getId())
                .title(chatFlow.getTitle())
                .description(chatFlow.getDescription())
                .author(UserResponse.from(chatFlow.getAuthor()))
                .thumbnail(chatFlow.getThumbnail())
                .categories(categories.stream()
                        .map(CategoryResponse::from)
                        .toList())
                .isPublic(chatFlow.isPublic())
                .build();
    }

}
