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
public class Answer extends Node {

    @Lob
    private String output_message;

    @Builder
    private Answer(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String output_message) {
        super(id, chatFlow, name, type, coordinate);
        this.output_message = output_message;
    }

    public static Answer create(ChatFlow chatFlow, Coordinate coordinate) {
        return Answer.builder()
            .chatFlow(chatFlow)
            .name("Answer")
            .type(NodeType.ANSWER)
            .coordinate(coordinate)
            .build();
    }

}
