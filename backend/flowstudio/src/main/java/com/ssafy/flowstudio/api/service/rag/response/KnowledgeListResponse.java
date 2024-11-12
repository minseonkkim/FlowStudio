package com.ssafy.flowstudio.api.service.rag.response;

import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class KnowledgeListResponse {
    private final Long knowledgeId;
    private final String title;
    private final Boolean isPublic;
    private final String createdAt;
    private final Integer totalToken;

    @Builder
    private KnowledgeListResponse(Long knowledgeId, String title, Boolean isPublic, LocalDateTime createdAt, Integer totalToken) {
        this.knowledgeId = knowledgeId;
        this.title = title;
        this.isPublic = isPublic;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.totalToken = totalToken;
    }

    public static KnowledgeListResponse from(Knowledge knowledge) {
        return KnowledgeListResponse.builder()
                .knowledgeId(knowledge.getId())
                .title(knowledge.getTitle())
                .isPublic(knowledge.isPublic())
                .createdAt(knowledge.getCreatedAt())
                .totalToken(knowledge.getTotalToken())
                .build();
    }

}
