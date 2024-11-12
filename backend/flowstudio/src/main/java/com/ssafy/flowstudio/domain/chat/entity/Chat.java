package com.ssafy.flowstudio.domain.chat.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_flow_id", nullable = false)
    private ChatFlow chatFlow;

    @Column
    private String title;

    @Column
    private boolean isPreview;

    @Lob
    private String messageList;

    @Builder
    private Chat(Long id, User user, ChatFlow chatFlow, String title, boolean isPreview, String messageList) {
        this.id = id;
        this.user = user;
        this.chatFlow = chatFlow;
        this.title = title;
        this.isPreview = isPreview;
        this.messageList = messageList;
    }

    public static Chat create(User user, ChatFlow chatFlow, boolean isPreview) {
        return Chat.builder()
                .user(user)
                .chatFlow(chatFlow)
                .isPreview(isPreview)
                .messageList("[]")
                .build();
    }

    public void updateHistory(String updatedChatHistory) {
        this.messageList = updatedChatHistory;
    }

    public void updateChatFlow(ChatFlow publishChatFlow) {
        this.chatFlow = publishChatFlow;
    }
}
