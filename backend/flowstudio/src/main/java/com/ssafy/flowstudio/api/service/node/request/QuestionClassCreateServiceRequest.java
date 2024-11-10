package com.ssafy.flowstudio.api.service.node.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionClassCreateServiceRequest {
    private final String content;

    @Builder
    public QuestionClassCreateServiceRequest(String content) {
        this.content = content;
    }
}
