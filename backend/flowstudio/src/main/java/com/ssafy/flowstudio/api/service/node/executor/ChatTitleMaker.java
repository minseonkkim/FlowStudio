package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ChatTitleMaker {

    private final SseEmitters sseEmitters;
    private final ChatRepository chatRepository;

    @Async
    @Transactional
    public void makeTitle(Chat chat, ChatLanguageModel chatModel, String promptUser) {
        List<ChatMessage> titleMessage = new ArrayList<>();
        String systemMessage = "입력된 문장을 요약해서 제목을 만들거야. 반드시 10글자 이하로 핵심만 간단하고 간결하게 요약해";
        titleMessage.add(new SystemMessage(systemMessage));
        titleMessage.add(new UserMessage(promptUser));

        Response<AiMessage> titleResponse = chatModel.generate(titleMessage);
        String title = titleResponse.content().text();

        chat.updateTitle(title);
        chatRepository.save(chat);

        sseEmitters.sendTitle(chat, title);
    }

}
