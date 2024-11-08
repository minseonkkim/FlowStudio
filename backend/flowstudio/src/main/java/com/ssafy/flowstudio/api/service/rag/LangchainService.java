package com.ssafy.flowstudio.api.service.rag;

import com.ssafy.flowstudio.api.service.rag.request.LangchainClient;
import com.ssafy.flowstudio.api.service.rag.request.TextSplitServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LangchainService {
    private final LangchainClient langchainClient;

    public List<String> getSplitText(int chunkSize, int chunkOverlap, String text) {
        return langchainClient.getSplitText(TextSplitServiceRequest.builder()
                        .text(text)
                        .chunkSize(chunkSize)
                        .chunkOverlap(chunkOverlap)
                        .build())
                .getChunks();
    }
}
