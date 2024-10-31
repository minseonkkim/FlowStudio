package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KnowledgeResponse {

    private final Long knowledgeId;
    private final String title;

    @Builder
    private KnowledgeResponse(Long knowledgeId, String title) {
        this.knowledgeId = knowledgeId;
        this.title = title;
    }

    public static KnowledgeResponse from(Knowledge knowledge) {
        return KnowledgeResponse.builder()
                .knowledgeId(knowledge.getId())
                .title(knowledge.getTitle())
                .build();
    }

}
