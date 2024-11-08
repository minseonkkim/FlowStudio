package com.ssafy.flowstudio.api.controller.chat.request;

import com.ssafy.flowstudio.api.service.chat.request.ChatCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatCreateRequest {

    @NotNull(message = "미리보기 여부는 필수입니다.")
    private boolean isPreview;

    @Builder
    private ChatCreateRequest(boolean isPreview) {
        this.isPreview = isPreview;
    }

    public ChatCreateServiceRequest toServiceRequest() {
        return ChatCreateServiceRequest.builder()
                .isPreview(isPreview)
                .build();
    }

}
