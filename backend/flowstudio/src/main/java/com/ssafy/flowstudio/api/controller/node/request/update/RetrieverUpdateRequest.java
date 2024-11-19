package com.ssafy.flowstudio.api.controller.node.request.update;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.service.node.request.update.RetrieverUpdateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RetrieverUpdateRequest {
    @NotNull(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "좌표를 입력해주세요.")
    private CoordinateRequest coordinate;
    private Long knowledgeId;
    @NotNull(message = "intervalTime을 입력해주세요.")
    private Integer intervalTime = 1;
    @NotNull(message = "topK를 입력해주세요.")
    private Integer topK = 3;
    @NotNull(message = "scoreThreshold을 입력해주세요.")
    private Float scoreThreshold = 0f;
    @NotNull(message = "query를 입력해주세요.")
    private String query = "";

    @Builder
    private RetrieverUpdateRequest(String name, CoordinateRequest coordinate, Long knowledgeId, Integer intervalTime, Integer topK, Float scoreThreshold, String query) {
        this.name = name;
        this.coordinate = coordinate;
        this.knowledgeId = knowledgeId;
        this.intervalTime = intervalTime;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
        this.query = query;
    }

    public RetrieverUpdateServiceRequest toServiceRequest() {
        return RetrieverUpdateServiceRequest.builder()
                .name(name)
                .coordinate(coordinate.toServiceRequest())
                .knowledgeId(knowledgeId)
                .intervalTime(intervalTime)
                .topK(topK)
                .scoreThreshold(scoreThreshold)
                .query(query)
                .build();
    }
}
