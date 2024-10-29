package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VariableAssigner extends Node {

    @Column
    private String targetVariableName;

    @Column
    private String targetVariableType;

    @Builder
    private VariableAssigner(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String targetVariableName, String targetVariableType) {
        super(id, chatFlow, name, type, coordinate);
        this.targetVariableName = targetVariableName;
        this.targetVariableType = targetVariableType;
    }

    public static VariableAssigner create(ChatFlow chatFlow, Coordinate coordinate) {
        return VariableAssigner.builder()
            .chatFlow(chatFlow)
            .name("Variable Assigner")
            .type(NodeType.VARIABLE_ASSIGNER)
            .coordinate(coordinate)
            .build();
    }

}
