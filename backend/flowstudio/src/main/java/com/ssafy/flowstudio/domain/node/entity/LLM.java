package com.ssafy.flowstudio.domain.node.entity;

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
    private LLM(Long id, String name, NodeType type, Coordinate coordinate, String output_key, String prompt_system, String prompt_user, String context, String model_param_list) {
        super(id, name, type, coordinate, output_key);
        this.prompt_system = prompt_system;
        this.prompt_user = prompt_user;
        this.context = context;
        this.model_param_list = model_param_list;
    }

    public static LLM create(Coordinate coordinate, String output_key) {
        return LLM.builder()
            .name("LLM")
            .type(NodeType.LLM)
            .coordinate(coordinate)
            .output_key(output_key)
            .build();
    }

}
