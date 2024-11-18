package com.ssafy.flowstudio.api.controller.rag.request;

import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class KnowledgeRequest {

    private String title;
    private Boolean isPublic;

    @Builder
    private KnowledgeRequest(String title, Boolean isPublic) {
        this.title = title;
        this.isPublic = isPublic;
    }

    public KnowledgeServiceRequest toServiceRequest() {
        return KnowledgeServiceRequest.builder()
                .title(title)
                .isPublic(isPublic)
                .build();
    }
}
