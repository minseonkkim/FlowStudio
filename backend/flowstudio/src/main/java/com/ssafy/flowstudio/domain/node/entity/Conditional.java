package com.ssafy.flowstudio.domain.node.entity;


import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Conditional extends Node {

    @Lob
    private String sub_conditional_list;

    @Builder
    private Conditional(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String sub_conditional_list) {
        super(id, chatFlow, name, type, coordinate);
        this.sub_conditional_list = sub_conditional_list;
    }

    public static Conditional create(ChatFlow chatFlow, Coordinate coordinate) {
        return Conditional.builder()
            .chatFlow(chatFlow)
            .name("Conditional")
            .type(NodeType.CONDITIONAL)
            .coordinate(coordinate)
            .build();
    }

}
