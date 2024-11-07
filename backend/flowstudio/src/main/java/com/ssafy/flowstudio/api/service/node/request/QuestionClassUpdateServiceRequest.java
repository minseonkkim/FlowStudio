package com.ssafy.flowstudio.api.service.node.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionClassUpdateServiceRequest {
    private final String content;
    private final Long edgeId;

    @Builder
    public QuestionClassUpdateServiceRequest(String content, Long edgeId) {
        this.content = content;
        this.edgeId = edgeId;
    }
}
