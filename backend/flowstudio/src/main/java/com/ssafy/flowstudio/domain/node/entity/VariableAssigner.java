package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.entity.GlobalVariable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VariableAssigner extends Node {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_variable_id")
    private GlobalVariable globalVariable;

    @Builder
    private VariableAssigner(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, GlobalVariable globalVariable) {
        super(id, chatFlow, name, type, coordinate);
        this.globalVariable = globalVariable;
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
