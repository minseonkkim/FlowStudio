package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LlmDetailResponse extends NodeResponse {
    private final String promptSystem;
    private final String promptUser;

    @Builder
    public LlmDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, String promptSystem, String promptUser) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges);
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
    }

    public static LlmDetailResponse from(LLM llm) {
        return LlmDetailResponse.builder()
                .nodeId(llm.getId())
                .name(llm.getName())
                .type(llm.getType())
                .coordinate(CoordinateResponse.from(llm.getCoordinate()))
                .outputEdges(llm.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(llm.getInputEdges().stream().map(EdgeResponse::from).toList())
                .promptSystem(llm.getPromptSystem())
                .promptUser(llm.getPromptUser())
                .build();
    }

}
