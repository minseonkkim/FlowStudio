package com.ssafy.flowstudio.api.service.rag.response;

import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class KnowledgeSearchResponse {

    private final Long knowledgeId;
    private final Long userId;
    private final Boolean isPublic;

    @Builder
    public KnowledgeSearchResponse(Long knowledgeId, Long userId, Boolean isPublic) {
        this.knowledgeId = knowledgeId;
        this.userId = userId;
        this.isPublic = isPublic;
    }

    public static KnowledgeSearchResponse from(Knowledge knowledge) {
        return KnowledgeSearchResponse.builder()
                .knowledgeId(knowledge.getId())
                .userId(knowledge.getUser().getId())
                .isPublic(knowledge.isPublic())
                .build();
    }
}
