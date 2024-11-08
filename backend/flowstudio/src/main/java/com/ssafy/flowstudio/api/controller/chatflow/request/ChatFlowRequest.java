package com.ssafy.flowstudio.api.controller.chatflow.request;

import com.ssafy.flowstudio.api.service.chatflow.request.ChatFlowServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ChatFlowRequest {

    @NotNull(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "아이콘은 필수입니다.")
    private String thumbnail;
    private String description;
    private List<Long> categoryIds;


    @Builder
    private ChatFlowRequest(List<Long> categoryIds, String title, String description, String thumbnail) {
        this.categoryIds = categoryIds;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public ChatFlowServiceRequest toServiceRequest() {
        return ChatFlowServiceRequest.builder()
                .title(title)
                .thumbnail(thumbnail)
                .description(description)
                .categoryIds(categoryIds)
                .build();
    }
}
