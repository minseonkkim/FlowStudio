package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.model.Model;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.stream.Mode;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = true)
    private Model model;

    @Builder
    private LLM(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String promptSystem, String promptUser, String context, String modelParamList, Model model) {
        super(id, chatFlow, name, type, coordinate);
        this.promptSystem = promptSystem;
        this.promptUser = promptUser;
        this.context = context;
        this.modelParamList = modelParamList;
        this.model = model;
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
    public void accept(NodeVisitor visitor, Chat chat) {
        visitor.visit(this, chat);
    }
}
