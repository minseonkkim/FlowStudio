package com.ssafy.flowstudio.api.service.chatflowtest;

import com.ssafy.flowstudio.api.service.chat.ChatService;
import com.ssafy.flowstudio.api.service.chat.request.ChatMessageServiceRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestServiceRequest;
import com.ssafy.flowstudio.api.service.node.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestExecutor {

    private final ChatService chatService;

    public void execute(Long chatId, ChatFlowTestServiceRequest request) {
        chatService.sendMessage(chatId, ChatMessageServiceRequest.from(request.getTestQuestion()));
    }

}
