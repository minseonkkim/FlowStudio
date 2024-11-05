package com.ssafy.flowstudio.api.controller.sse;

import com.ssafy.flowstudio.api.controller.sse.response.SseResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
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
        SseResponse data = SseResponse.from(node);

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

}
