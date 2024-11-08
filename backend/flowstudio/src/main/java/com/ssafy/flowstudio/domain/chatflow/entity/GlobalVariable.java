package com.ssafy.flowstudio.domain.chatflow.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GlobalVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "global_variable_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_flow_id", nullable = false)
    private ChatFlow chatFlow;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GlobalVariableType type;

    @Builder
    private GlobalVariable(Long id, ChatFlow chatFlow, String name, String value, GlobalVariableType type) {
        this.id = id;
        this.chatFlow = chatFlow;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public static GlobalVariable create(ChatFlow chatFlow, String name, String value, GlobalVariableType type) {
        return GlobalVariable.builder()
            .chatFlow(chatFlow)
            .name(name)
            .value(value)
            .type(type)
            .build();
    }

}
