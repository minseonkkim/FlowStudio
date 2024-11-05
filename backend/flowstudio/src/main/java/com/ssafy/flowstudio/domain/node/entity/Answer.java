package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
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
    private String outputMessage;

    @Builder
    private Answer(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate, String outputMessage) {
        super(id, chatFlow, name, type, coordinate);
        this.outputMessage = outputMessage;
    }

    public static Answer create(ChatFlow chatFlow, Coordinate coordinate) {
        return Answer.builder()
            .chatFlow(chatFlow)
            .name("Answer")
            .type(NodeType.ANSWER)
            .coordinate(coordinate)
            .build();
    }

    @Override
    public void accept(NodeVisitor visitor, Chat chat) {
        visitor.visit(this, chat);
    }
}
