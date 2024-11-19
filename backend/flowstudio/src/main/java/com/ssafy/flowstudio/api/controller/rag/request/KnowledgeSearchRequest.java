package com.ssafy.flowstudio.api.controller.rag.request;

import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeSearchServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeSearchResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class KnowledgeSearchRequest {

    @NotNull(message = "지식베이스 아이디는 필수입니다.")
    private long knowledgeId;
    private int interval;
    private int topK;
    private float scoreThreshold;
    @NotNull(message = "키워드는 필수입니다.")
    private String query;

    @Builder
    public KnowledgeSearchRequest(long knowledgeId, int interval, int topK, float scoreThreshold, String query) {
        this.knowledgeId = knowledgeId;
        this.interval = interval;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
        this.query = query;
    }

}
