package com.ssafy.flowstudio.api.service.rag.response;

import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class KnowledgeListResponse {
    private final Long knowledgeId;
    private final String title;
    private final Boolean isPublic;
    private final String createAt;

    @Builder
    private KnowledgeListResponse(Long knowledgeId, String title, Boolean isPublic, LocalDateTime createAt) {
        this.knowledgeId = knowledgeId;
        this.title = title;
        this.isPublic = isPublic;
        this.createAt = createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static KnowledgeListResponse from(Knowledge knowledge) {
        return KnowledgeListResponse.builder()
                .knowledgeId(knowledge.getId())
                .title(knowledge.getTitle())
                .isPublic(knowledge.isPublic())
                .createAt(knowledge.getCreatedAt())
                .build();
    }

}
