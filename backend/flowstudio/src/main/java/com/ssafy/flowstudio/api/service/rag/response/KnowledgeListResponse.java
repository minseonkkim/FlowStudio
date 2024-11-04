package com.ssafy.flowstudio.api.service.rag.response;

import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class KnowledgeListResponse {
    private final Long knowledgeId;
    private final String title;

    @Builder
    private KnowledgeListResponse(Long knowledgeId, String title) {
        this.knowledgeId = knowledgeId;
        this.title = title;
    }

    public static KnowledgeListResponse from(Knowledge knowledge) {
        return KnowledgeListResponse.builder()
                .knowledgeId(knowledge.getId())
                .title(knowledge.getTitle())
                .build();
    }

}
