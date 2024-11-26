package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.service.user.ApiKeyService;
import com.ssafy.flowstudio.common.secret.SecretKeyProperties;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.ModelProvider;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatModelFactory {

    private final SecretKeyProperties secretKeyProperties;
    private final ApiKeyService apiKeyService;

    public ChatLanguageModel createChatModel(Chat chat, LLM node) {
        String apiKey = getApiKey(chat, node);
        String modelName = node.getModelName().getName();
        double temperature = node.getTemperature();
        int maxTokens = node.getMaxTokens();

        return switch (node.getModelName().getProvider()) {
            case OPENAI -> OpenAiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .temperature(temperature)
                    .maxTokens(maxTokens)
                    .build();
            case ANTHROPIC -> AnthropicChatModel.builder()
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .temperature(temperature)
                    .maxTokens(maxTokens)
                    .build();
        };
    }

    public String getApiKey(Chat chat, LLM node) {
        boolean isPreview = chat.isPreview();
        ModelProvider provider = node.getModelName().getProvider();

        if (isPreview) {
            return switch (provider) {
                case OPENAI -> secretKeyProperties.getOpenAi();
                case ANTHROPIC -> secretKeyProperties.getClaude();
            };
        } else {
            return switch (provider) {
                case OPENAI -> apiKeyService.decrypt(chat.getChatFlow().getOwner().getApiKey().getOpenAiKey());
                case ANTHROPIC -> apiKeyService.decrypt(chat.getChatFlow().getOwner().getApiKey().getClaudeKey());
            };
        }
    }

}
