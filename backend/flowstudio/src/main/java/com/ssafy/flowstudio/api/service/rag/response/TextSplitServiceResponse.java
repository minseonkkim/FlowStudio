package com.ssafy.flowstudio.api.service.rag.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TextSplitServiceResponse {

    private List<String> chunks;

    @Builder
    public TextSplitServiceResponse(List<String> chunks) {
        this.chunks = chunks;
    }


}
