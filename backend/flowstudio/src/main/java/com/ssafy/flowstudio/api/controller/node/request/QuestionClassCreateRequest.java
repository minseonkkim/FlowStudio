package com.ssafy.flowstudio.api.controller.node.request;

import com.ssafy.flowstudio.api.service.node.request.QuestionClassCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionClassCreateRequest {

    @NotNull(message = "질문 분류의 내용은 필수입니다.")
    private String content;

    @Builder
    private QuestionClassCreateRequest(String content) {
        this.content = content;
    }

    public QuestionClassCreateServiceRequest toServiceRequest() {
        return QuestionClassCreateServiceRequest.builder()
                .content(content)
                .build();
    }
}
