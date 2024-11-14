package com.ssafy.flowstudio.api.controller.sse.response;

import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestResponse;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SseChatFlowTestResponse {

    private final Long chatId;
    private final Float embeddingDistance;
    private final Float crossEncoder;
    private final Float rougeMetric;

    @Builder
    private SseChatFlowTestResponse(Long chatId, Float embeddingDistance, Float crossEncoder, Float rougeMetric) {
        this.chatId = chatId;
        this.embeddingDistance = embeddingDistance;
        this.crossEncoder = crossEncoder;
        this.rougeMetric = rougeMetric;
    }

    public static SseChatFlowTestResponse of(Chat chat, ChatFlowTestResponse chatFlowTestResponse) {
        return SseChatFlowTestResponse.builder()
                .chatId(chat.getId())
                .embeddingDistance(chatFlowTestResponse.getEmbeddingDistance())
                .crossEncoder(chatFlowTestResponse.getCrossEncoder())
                .rougeMetric(chatFlowTestResponse.getRougeMetric())
                .build();
    }

}
