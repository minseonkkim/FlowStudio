package com.ssafy.flowstudio.api.service.rag.response;

import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
public class KnowledgeResponse {

    private final Long knowledgeId;
    private final String title;
    private final Boolean isPublic;
    private final String createAt;

    @Builder
    public KnowledgeResponse(Long knowledgeId, String title, Boolean isPublic, LocalDateTime createAt) {
        this.knowledgeId = knowledgeId;
        this.title = title;
        this.isPublic = isPublic;
        this.createAt = createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static KnowledgeResponse from(Knowledge knowledge) {
        return KnowledgeResponse.builder()
                .knowledgeId(knowledge.getId())
                .title(knowledge.getTitle())
                .isPublic(knowledge.isPublic())
                .createAt(knowledge.getCreatedAt())
                .build();
    }
}
