package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LLM extends Node {

    @Lob
    private String promptSystem;

    @Lob
    private String promptUser;

    @Column
    private String context;

    @Lob
    private String modelParamList;

    @Builder
    private LLM(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String promptSystem, String promptUser, String context, String modelParamList) {
        super(id, chatFlow, name, type, coordinate);
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.modelParamList = modelParamList;
    }

    public static LLM create(ChatFlow chatFlow, Coordinate coordinate) {
        return LLM.builder()
            .chatFlow(chatFlow)
            .name("LLM")
            .type(NodeType.LLM)
            .coordinate(coordinate)
            .build();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
