package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.document.entity.Document;
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
    private Document document;

    @Builder
    public Retriever(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, Document document) {
        super(id, chatFlow, name, type, coordinate);
        this.document = document;
    }

    public static Retriever create(ChatFlow chatFlow, Coordinate coordinate) {
        return Retriever.builder()
            .chatFlow(chatFlow)
            .coordinate(coordinate)
            .build();
    }

}
