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
public class Start extends Node {

    @Column
    private int maxLength;

    @Builder
    private Start(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, int maxLength) {
        super(id, chatFlow, name, type, coordinate);
        this.maxLength = maxLength;
    }

    public static Start create(ChatFlow chatFlow, Coordinate coordinate) {
        return Start.builder()
                .chatFlow(chatFlow)
                .name("Start")
                .type(NodeType.START)
                .coordinate(coordinate)
                .build();
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
