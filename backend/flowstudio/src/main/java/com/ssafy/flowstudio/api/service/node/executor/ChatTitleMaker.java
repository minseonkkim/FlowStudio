package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ChatTitleMaker {

    private final SseEmitters sseEmitters;

    @Async
    public void makeTitle(Chat chat, ChatLanguageModel chatModel, String promptUser) {
        List<ChatMessage> titleMessage = new ArrayList<>();
        titleMessage.add(new SystemMessage("다음 문장을 제목으로 사용할건데 10글자 이하로 핵심만 요약해"));
        titleMessage.add(new UserMessage(promptUser));

        Response<AiMessage> titleResponse = chatModel.generate(titleMessage);
        String title = titleResponse.content().text();

        chat.updateTitle(title);
        sseEmitters.sendTitle(chat, title);
    }

}
