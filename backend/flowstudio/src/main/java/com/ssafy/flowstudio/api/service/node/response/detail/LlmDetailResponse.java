package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.domain.node.entity.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LlmDetailResponse extends NodeDetailResponse {
    private final String promptSystem;
    private final String promptUser;
    private final String context;
    private final double temperature;
    private final int maxTokens;
    private final ModelProvider modelProvider;
    private final ModelName modelName;


    @Builder
    public LlmDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, String promptSystem, String promptUser, List<SimpleNodeResponse> precedingNodes, String context, double temperature, int maxTokens, ModelProvider modelProvider, ModelName modelName) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges, precedingNodes);
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.modelProvider = modelProvider;
        this.modelName = modelName;
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
                .context(llm.getContext())
                .temperature(llm.getTemperature())
                .maxTokens(llm.getMaxTokens())
                .modelProvider(llm.getModelProvider())
                .modelName(llm.getModelName())
                .build();
    }

}
