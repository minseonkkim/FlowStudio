package com.ssafy.flowstudio.api.service.node.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionClassUpdateServiceRequest {
    private final String content;

    @Builder
    public QuestionClassUpdateServiceRequest(String content, Long edgeId) {
        this.content = content;
    }
}
