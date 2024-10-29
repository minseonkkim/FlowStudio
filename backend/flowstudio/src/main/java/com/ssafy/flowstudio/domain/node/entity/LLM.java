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
    private String prompt_system;

    @Lob
    private String prompt_user;

    @Column
    private String context;

    @Lob
    private String model_param_list;

    @Builder
    private LLM(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String prompt_system, String prompt_user, String context, String model_param_list) {
        super(id, chatFlow, name, type, coordinate);
        this.prompt_system = prompt_system;
        this.prompt_user = prompt_user;
        this.context = context;
        this.model_param_list = model_param_list;
    }

    public static LLM create(ChatFlow chatFlow, Coordinate coordinate) {
        return LLM.builder()
            .chatFlow(chatFlow)
            .name("LLM")
            .type(NodeType.LLM)
            .coordinate(coordinate)
            .build();
    }

}
