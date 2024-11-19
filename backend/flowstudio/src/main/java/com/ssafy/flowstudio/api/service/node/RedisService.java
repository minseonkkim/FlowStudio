package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisService {

    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final int EXPIRATION_MINUTES = 5;

    public String get(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.info(String.format("Redis does not have value matching Key: %s", key));
        }
        return value;
    }

    public String get(Long chatId, Long nodeId) {
        String key = chatId + ":" + nodeId;
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.info(String.format("Redis does not have value matching Key: %s", key));
        }
        return value;
    }

    public String get(Long chatId, ChatEnvVariable varName) {
        String key = chatId + ":" + varName;
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.info(String.format("Redis does not have value matching Key: %s", key));
        }
        return value;
    }

    public void saveTestValue(Long chatId, String value) {
        String key = "test:" + chatId;
        redisTemplate.opsForValue().set(key, value, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    public void save(Long chatId, Long nodeId, String value) {
        String key = chatId + ":" + nodeId;
        redisTemplate.opsForValue().set(key, value, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    public void save(Long chatId, ChatEnvVariable varName, String value) {
        String key = chatId + ":" + varName;
        redisTemplate.opsForValue().set(key, value, EXPIRATION_MINUTES, TimeUnit.MINUTES);
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
