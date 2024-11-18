package com.ssafy.flowstudio.domain.node.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelName {
    // OpenAI
    GPT_4_O("gpt-4o"),
    GPT_4_O_MINI("gpt-4o-mini"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),

    CLAUDE_2("claude-2.0"),
    CLAUDE_3_SONNET_20240229("claude-3-sonnet-20240229"),
    CLAUDE_3_5_SONNET_20240620("claude-3-5-sonnet-20240620"),
    ;

    private final String stringValue;
}
