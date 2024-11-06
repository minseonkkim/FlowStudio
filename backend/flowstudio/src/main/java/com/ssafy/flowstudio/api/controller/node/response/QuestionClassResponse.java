package com.ssafy.flowstudio.api.controller.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionClassResponse {
    private final Long id;
    private final String content;
    private final EdgeResponse edge;
    private final Long questionClassifierId;

    @Builder
    public QuestionClassResponse(Long id, String content, EdgeResponse edge, Long questionClassifierId) {
        this.id = id;
        this.content = content;
        this.edge = edge;
        this.questionClassifierId = questionClassifierId;
    }

    public static QuestionClassResponse from(QuestionClass questionClass) {
        return QuestionClassResponse.builder()
                .id(questionClass.getId())
                .content(questionClass.getContent())
                .edge((questionClass.getEdge() == null) ? null : EdgeResponse.from(questionClass.getEdge()))
                .questionClassifierId(questionClass.getQuestionClassifier().getId())
                .build();
    }
}
