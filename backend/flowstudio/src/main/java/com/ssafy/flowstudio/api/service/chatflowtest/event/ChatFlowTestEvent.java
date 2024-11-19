package com.ssafy.flowstudio.api.service.chatflowtest.event;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChatFlowTestEvent extends ApplicationEvent {

    private final Chat chat;

    @Builder
    private ChatFlowTestEvent(Object source, Chat chat) {
        super(source);
        this.chat = chat;
    }

    public static ChatFlowTestEvent of(Object source, Chat chat) {
        return ChatFlowTestEvent.builder()
                .source(source)
                .chat(chat)
                .build();
    }

}
