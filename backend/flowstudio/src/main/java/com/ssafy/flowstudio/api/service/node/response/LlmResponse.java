package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LlmResponse extends NodeResponse {

    private final String promptSystem;
    private final String promptUser;

    @Builder
    private LlmResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> sourceEdges, List<EdgeResponse> targetEdges, String promptSystem, String promptUser) {
        super(nodeId, name, type, coordinate, sourceEdges, targetEdges);
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
    }

    public static LlmResponse from(LLM llm) {
        return LlmResponse.builder()
                .nodeId(llm.getId())
                .name(llm.getName())
                .type(llm.getType())
                .coordinate(CoordinateResponse.from(llm.getCoordinate()))
                .sourceEdges(llm.getSourceEdges().stream().map(EdgeResponse::from).toList())
                .targetEdges(llm.getTargetEdges().stream().map(EdgeResponse::from).toList())
                .promptSystem(llm.getPromptSystem())
                .promptUser(llm.getPromptUser())
                .build();
    }

}
