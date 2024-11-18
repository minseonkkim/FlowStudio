package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import jakarta.persistence.*;
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

    @Column
    private double temperature;

    @Column
    private int maxTokens;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelProvider modelProvider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelName modelName;

    @Builder
    private LLM(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String promptSystem, String promptUser, String context, double temperature, int maxTokens, ModelProvider modelProvider, ModelName modelName) {
        super(id, chatFlow, name, type, coordinate);
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.modelProvider = modelProvider;
        this.modelName = modelName;
    }

    public static LLM create(ChatFlow chatFlow, Coordinate coordinate) {
        return LLM.builder()
                .chatFlow(chatFlow)
                .name("LLM")
                .type(NodeType.LLM)
                .coordinate(coordinate)
                .temperature(0.7)
                .maxTokens(512)
                .promptSystem("")
                .promptUser("")
                .modelProvider(ModelProvider.OPENAI)
                .modelName(ModelName.GPT_4_O_MINI)
                .build();
    }

    @Override
    public void accept(NodeVisitor visitor, Chat chat) {
        visitor.visit(this, chat);
    }

    public void update(String name, Coordinate coordinate, String promptSystem, String promptUser, String context, Double temperature, Integer maxTokens) {
        this.name = name;
        this.coordinate = coordinate;
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    public void updatePrompt(String systemPrompt, String userPrompt) {
        this.promptSystem = systemPrompt;
        this.promptUser = userPrompt;
    }
}
