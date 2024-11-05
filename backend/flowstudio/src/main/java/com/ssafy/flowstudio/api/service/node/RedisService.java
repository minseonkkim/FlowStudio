package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public String get(Long chatId, Long nodeId) {
        String key = chatId + ":" + nodeId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public String get(Long chatId, ChatEnvVariable varName) {
        String key = chatId + ":" + varName;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void save(Long chatId, Long nodeId, String value) {
        String key = chatId + ":" + nodeId;
        redisTemplate.opsForValue().set(key, value);
    }

    public void save(Long chatId, ChatEnvVariable varName, String value) {
        String key = chatId + ":" + varName;
        redisTemplate.opsForValue().set(key, value);
    }

    public void deleteAll(Long chatId) {
        Set<String> keys = redisTemplate.keys(chatId + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }


    public boolean exists(Long chatId, Long nodeId) {
        String key = chatId + ":" + nodeId;
        return redisTemplate.hasKey(key);
    }

}
