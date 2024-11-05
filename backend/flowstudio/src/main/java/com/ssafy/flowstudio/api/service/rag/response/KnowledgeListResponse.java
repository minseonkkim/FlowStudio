package com.ssafy.flowstudio.api.service.rag.response;

import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class KnowledgeListResponse {
    private final Long knowledgeId;
    private final String title;
    private final Boolean isPublic;

    @Builder
    private KnowledgeListResponse(Long knowledgeId, String title, Boolean isPublic) {
        this.knowledgeId = knowledgeId;
        this.title = title;
        this.isPublic = isPublic;
    }

    public static KnowledgeListResponse from(Knowledge knowledge) {
        return KnowledgeListResponse.builder()
                .knowledgeId(knowledge.getId())
                .title(knowledge.getTitle())
                .isPublic(knowledge.isPublic())
                .build();
    }

}
