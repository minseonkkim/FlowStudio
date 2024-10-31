package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RetrieverResponse extends NodeResponse {

    private final KnowledgeResponse knowledge;

    @Builder
    public RetrieverResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, KnowledgeResponse knowledge) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges);
        this.knowledge = knowledge;
    }

    public static RetrieverResponse from(Retriever retriever) {
        return RetrieverResponse.builder()
                .nodeId(retriever.getId())
                .name(retriever.getName())
                .type(retriever.getType())
                .coordinate(CoordinateResponse.from(retriever.getCoordinate()))
                .outputEdges(retriever.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(retriever.getInputEdges().stream().map(EdgeResponse::from).toList())
                .knowledge(KnowledgeResponse.from(retriever.getKnowledge()))
                .build();
    }

}
