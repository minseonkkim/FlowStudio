package com.ssafy.flowstudio.api.controller.sse;

import com.ssafy.flowstudio.api.controller.sse.response.SseChatFlowTestPredictionResponse;
import com.ssafy.flowstudio.api.controller.sse.response.SseChatFlowTestResponse;
import com.ssafy.flowstudio.api.controller.sse.response.SseNodeResponse;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestResponse;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.api.controller.sse.response.SseTitleResponse;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Slf4j
public class SseEmitters {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(User user, SseEmitter emitter) {
        Long userId = user.getId();

        this.emitters.put(userId, emitter);
        log.info("new emitter added for user {}: {}", userId, emitter);
        log.info("emitter list size: {}", emitters.size());

        emitter.onCompletion(() -> {
            log.info("onCompletion callback for user {}", userId);
            this.emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback for user {}", userId);
            emitter.complete();
        });

        return emitter;
    }

    public void send(User user, Node node) {
        SseNodeResponse data = SseNodeResponse.from(node);

        SseEmitter emitter = emitters.get(user.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("node")
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendTitle(Chat chat, String title) {
        SseTitleResponse data = SseTitleResponse.of(chat.getId(), title);

        SseEmitter emitter = emitters.get(chat.getUser().getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("title")
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(User user, Node node, String message) {
        SseNodeResponse data = SseNodeResponse.of(node, message);

        SseEmitter emitter = emitters.get(user.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("node")
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendChatFlowTestResult(Chat chat, ChatFlowTestResponse response) {
        SseChatFlowTestResponse data = SseChatFlowTestResponse.of(chat, response);

        SseEmitter emitter = emitters.get(chat.getUser().getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("result")
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendChatFlowTestLlm(Chat chat, String message) {
        SseChatFlowTestPredictionResponse data = SseChatFlowTestPredictionResponse.of(chat.getId(), message);

        SseEmitter emitter = emitters.get(chat.getUser().getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("prediction")
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Scheduled(fixedRate = 30000)
    public void sendHeartbeat() {
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("heartbeat")
                        .data("ping"));
            } catch (IOException e) {
                log.error("Failed to send heartbeat to user {}: {}", userId, e.getMessage());
                emitter.complete();
                emitters.remove(userId);
            }
        });
    }

    public Boolean isConnected(User user) {
        return emitters.containsKey(user.getId());
    }

}
