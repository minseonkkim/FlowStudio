package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RetrieverResponse extends NodeResponse {

    private final KnowledgeResponse knowledge;
    private final Integer intervalTime;
    private final Integer topK;
    private final Float scoreThreshold;
    private final String query;

    @Builder
    public RetrieverResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, KnowledgeResponse knowledge,
                             Integer intervalTime, Integer topK, Float scoreThreshold, String query) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges);
        this.knowledge = knowledge;
        this.intervalTime = intervalTime;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
        this.query = query;
    }

    public static RetrieverResponse from(Retriever retriever) {
        return RetrieverResponse.builder()
                .nodeId(retriever.getId())
                .name(retriever.getName())
                .type(retriever.getType())
                .coordinate(CoordinateResponse.from(retriever.getCoordinate()))
                .outputEdges(retriever.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(retriever.getInputEdges().stream().map(EdgeResponse::from).toList())
                .knowledge(retriever.getKnowledge() == null ? null : KnowledgeResponse.from(retriever.getKnowledge()))
                .intervalTime(retriever.getIntervalTime())
                .topK(retriever.getTopK())
                .scoreThreshold(retriever.getScoreThreshold())
                .query(retriever.getQuery())
                .build();
    }

}
