package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LlmDetailResponse extends NodeResponse {
    private final String promptSystem;
    private final String promptUser;

    @Builder
    public LlmDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, String promptSystem, String promptUser, List<SimpleNodeResponse> precedingNodes) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges, precedingNodes);
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
    }

    public static LlmDetailResponse of(LLM llm, List<Node> precedingNodes) {
        return LlmDetailResponse.builder()
                .nodeId(llm.getId())
                .name(llm.getName())
                .type(llm.getType())
                .coordinate(CoordinateResponse.from(llm.getCoordinate()))
                .outputEdges(llm.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(llm.getInputEdges().stream().map(EdgeResponse::from).toList())
                .precedingNodes(precedingNodes.stream().map(SimpleNodeResponse::from).toList())
                .promptSystem(llm.getPromptSystem())
                .promptUser(llm.getPromptUser())
                .build();
    }

}
