package com.ssafy.flowstudio.api.service.chatflowtest.event;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestResponse;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.api.service.rag.request.LangchainClient;
import com.ssafy.flowstudio.common.util.StatisticCalculator;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflowtest.ChatFlowTestRepository;
import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTestCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatFlowTestEventListener {

    private final SseEmitters sseEmitters;
    private final RedisService redisService;
    private final LangchainClient langchainClient;

    private final ChatFlowTestRepository chatFlowTestRepository;
    private final StatisticCalculator statisticCalculator;

    @EventListener
    public void handleChatFlowTestEvent(ChatFlowTestEvent event) {
        Chat chat = event.getChat();
        ChatFlowTest chatFlowTest = chat.getChatFlowTest();

        String prediction = redisService.get("test:" + chat.getId());
        ChatFlowTestRequest chatFlowTestRequest = ChatFlowTestRequest.of(chat.getGroundTruth(), prediction);
        ChatFlowTestResponse chatFlowTestResponse = langchainClient.chatFlowTest(chatFlowTestRequest);

        ChatFlowTestCase chatFlowTestCase = ChatFlowTestCase.create(
                chatFlowTest,
                chat.getTestQuestion(),
                chat.getGroundTruth(),
                prediction,
                chatFlowTestResponse.getEmbeddingDistance(),
                chatFlowTestResponse.getCrossEncoder(),
                chatFlowTestResponse.getRougeMetric()
        );
        chatFlowTest.addChatFlowTestCase(chatFlowTestCase);

        chatFlowTest.incrementSuccessCount();
        chatFlowTestRepository.save(chatFlowTest);

        // 결과 전송
        sseEmitters.sendChatFlowTestCaseResult(chat, chatFlowTestResponse);

        // 테스트 종료시 통계 계산
        if (chatFlowTest.isCompleted()) {
            List<Float> result = statisticCalculator.calculate(chatFlowTest.getChatFlowTestCases());
            chatFlowTest.updateResult(result);

            sseEmitters.sendChatFlowTestResult(chat, result);

            chatFlowTestRepository.save(chatFlowTest);
        }
    }

}
