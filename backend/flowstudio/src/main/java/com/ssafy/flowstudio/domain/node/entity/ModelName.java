package com.ssafy.flowstudio.domain.node.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelName {
    // OpenAI
    GPT_4_O("gpt-4o", ModelProvider.OPENAI, 16384),
    GPT_4_O_MINI("gpt-4o-mini", ModelProvider.OPENAI,16384),
    GPT_3_5_TURBO("gpt-3.5-turbo", ModelProvider.OPENAI,4096),

    CLAUDE_3_5_SONNET("claude-3-5-sonnet-latest", ModelProvider.ANTHROPIC,8192),
    CLAUDE_3_5_HAIKU("claude-3-5-haiku-latest", ModelProvider.ANTHROPIC,8192),
    ;

    private final String name;
    private final ModelProvider provider;
    private final int maxTokens;
}
