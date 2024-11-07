package com.ssafy.flowstudio.api.service.rag.request;

import com.ssafy.flowstudio.api.controller.rag.request.KnowledgeSearchRequest;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeSearchResponse;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class KnowledgeSearchServiceRequest {
    private final KnowledgeSearchResponse knowledge;
    private final int interval;
    private final int topK;
    private final float scoreThreshold;
    private final String query;

    @Builder
    public KnowledgeSearchServiceRequest(KnowledgeSearchResponse knowledge, int interval, int topK, float scoreThreshold, String query) {
        this.knowledge = knowledge;
        this.query = query;
        this.interval = interval;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
    }

    public static KnowledgeSearchServiceRequest from(KnowledgeSearchRequest request, KnowledgeSearchResponse knowledge) {
        return KnowledgeSearchServiceRequest.builder()
                .knowledge(knowledge)
                .interval(request.getInterval())
                .topK(request.getTopK())
                .query(request.getQuery())
                .scoreThreshold(request.getScoreThreshold())
                .build();
    }

}
