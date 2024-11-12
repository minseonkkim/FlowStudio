package com.ssafy.flowstudio.api.controller.node.request;

import com.ssafy.flowstudio.api.service.node.request.QuestionClassCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.QuestionClassUpdateServiceRequest;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionClassUpdateRequest {

    @NotBlank(message = "질문 분류의 내용은 필수입니다.")
    private String content;

    @Builder
    public QuestionClassUpdateRequest(String content, Long edgeId) {
        this.content = content;
    }

    public QuestionClassUpdateServiceRequest toServiceRequest() {
        return QuestionClassUpdateServiceRequest.builder()
                .content(content)
                .build();
    }
}
