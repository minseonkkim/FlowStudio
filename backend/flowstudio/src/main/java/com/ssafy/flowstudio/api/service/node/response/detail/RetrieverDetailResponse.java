package com.ssafy.flowstudio.api.service.node.response.detail;

import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RetrieverDetailResponse extends NodeDetailResponse {

    private final KnowledgeResponse knowledge;
    private final Float scoreThreshold;
    private final Integer topK;
    private final Integer intervalTime;
    private final String query;

    @Builder
    public RetrieverDetailResponse(Long nodeId, String name, NodeType type, CoordinateResponse coordinate, List<EdgeResponse> outputEdges, List<EdgeResponse> inputEdges, KnowledgeResponse knowledge, List<SimpleNodeResponse> precedingNodes,
                                   Float scoreThreshold, Integer intervalTime ,Integer topK, String query) {
        super(nodeId, name, type, coordinate, outputEdges, inputEdges, precedingNodes);
        this.knowledge = knowledge;
        this.scoreThreshold = scoreThreshold;
        this.topK = topK;
        this.intervalTime = intervalTime;
        this.query = query;
    }

    public static RetrieverDetailResponse of(Retriever retriever, List<Node> precedingNodes) {
        return RetrieverDetailResponse.builder()
                .nodeId(retriever.getId())
                .name(retriever.getName())
                .type(retriever.getType())
                .coordinate(CoordinateResponse.from(retriever.getCoordinate()))
                .outputEdges(retriever.getOutputEdges().stream().map(EdgeResponse::from).toList())
                .inputEdges(retriever.getInputEdges().stream().map(EdgeResponse::from).toList())
                .precedingNodes(precedingNodes.stream().map(SimpleNodeResponse::from).toList())
                .knowledge(retriever.getKnowledge() == null ? null : KnowledgeResponse.from(retriever.getKnowledge()))
                .scoreThreshold(retriever.getScoreThreshold())
                .topK(retriever.getTopK())
                .intervalTime(retriever.getIntervalTime())
                .query(retriever.getQuery())
                .build();
    }
}
