package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Retriever extends Node {

    @OneToOne(fetch = FetchType.LAZY)
    private Knowledge knowledge;

    @Builder
    public Retriever(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, Knowledge knowledge) {
        super(id, chatFlow, name, type, coordinate);
        this.knowledge = knowledge;
    }

    public static Retriever create(ChatFlow chatFlow, Coordinate coordinate) {
        return Retriever.builder()
            .chatFlow(chatFlow)
            .coordinate(coordinate)
            .build();
    }

}
