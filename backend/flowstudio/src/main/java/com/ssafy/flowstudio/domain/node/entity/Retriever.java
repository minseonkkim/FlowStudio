package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Retriever extends Node {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knowledge_id")
    private Knowledge knowledge;

    @Column
    private Integer intervalTime;

    @Column
    private Integer topK;

    @Column
    private Float scoreThreshold;

    @Column
    private String query;

    @Builder
    public Retriever(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, Knowledge knowledge
            , Integer intervalTime, Integer topK, Float scoreThreshold, String query
    ) {
        super(id, chatFlow, name, type, coordinate);
        this.knowledge = knowledge;
        this.intervalTime = intervalTime;
        this.topK = topK;
        this.scoreThreshold = scoreThreshold;
        this.query = query;
    }

    public static Retriever create(ChatFlow chatFlow, Coordinate coordinate, int intervalTime, int topK, float scoreThreshold) {
        return Retriever.builder()
                .chatFlow(chatFlow)
                .name("Retriever")
                .type(NodeType.RETRIEVER)
                .coordinate(coordinate)
                .intervalTime(intervalTime)
                .topK(topK)
                .scoreThreshold(scoreThreshold)
                .build();
    }

    public void update(String name, Coordinate coordinate, Knowledge knowledge, int intervalTime, float scoreThreshold, int topK, String query) {
        this.name = name;
        this.coordinate = coordinate;
        this.knowledge = knowledge;
        this.intervalTime = intervalTime;
        this.scoreThreshold = scoreThreshold;
        this.topK = topK;
        this.query = query;
    }

    public void updateKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    @Override
    public void accept(NodeVisitor visitor, Chat chat) {
        visitor.visit(this, chat);
    }
}
