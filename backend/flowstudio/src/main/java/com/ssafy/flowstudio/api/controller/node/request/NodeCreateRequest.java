package com.ssafy.flowstudio.api.controller.node.request;

import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class NodeCreateRequest {

    @NotNull(message = "챗플로우 아이디는 필수입니다.")
    private Long chatFlowId;

    @NotNull(message = "좌표는 필수입니다.")
    private CoordinateRequest coordinate;

    @NotNull(message = "노드 타입은 필수입니다.")
    private NodeType type;

    @Builder
    private NodeCreateRequest(Long chatFlowId, CoordinateRequest coordinate, NodeType type) {
        this.chatFlowId = chatFlowId;
        this.coordinate = coordinate;
        this.type = type;
    }

    public NodeCreateServiceRequest toServiceRequest() {
        return NodeCreateServiceRequest.builder()
                .chatFlowId(chatFlowId)
                .coordinate(coordinate.toServiceRequest())
                .type(type)
                .build();
    }

}
