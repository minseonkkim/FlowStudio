package com.ssafy.flowstudio.domain.chatflow.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatFlowCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_flow_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_flow_id", nullable = false)
    private ChatFlow chatFlow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder
    private ChatFlowCategory(Long id, ChatFlow chatFlow, Category category) {
        this.id = id;
        this.chatFlow = chatFlow;
        this.category = category;
    }

    public static ChatFlowCategory create(ChatFlow chatFlow, Category category) {
        return ChatFlowCategory.builder()
                .chatFlow(chatFlow)
                .category(category)
                .build();
    }

}
