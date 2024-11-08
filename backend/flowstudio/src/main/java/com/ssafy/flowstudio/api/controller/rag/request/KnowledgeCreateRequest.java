package com.ssafy.flowstudio.api.controller.rag.request;

import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class KnowledgeCreateRequest {

    @NotNull(message = "파일은 필수입니다.")
    private MultipartFile file;
    private int chunkSize;
    private int chunkOverlap;
    private String separators;


    @Builder
    public KnowledgeCreateRequest(MultipartFile file, int chunkSize, int chunkOverlap, String separators) {
        this.file = file;
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.separators = separators;
    }

    public KnowledgeCreateServiceRequest toServiceRequest() {
        return KnowledgeCreateServiceRequest.builder()
                .file(file)
                .chunkSize(chunkSize)
                .chunkOverlap(chunkOverlap)
                .separators(separators)
                .build();
    }
}
