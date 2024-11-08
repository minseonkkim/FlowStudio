package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class RedisServiceTest extends IntegrationTestSupport {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @DisplayName("Redis에 키를 저장한다.")
    @Test
    void save() {
        // given
        Long chatId = 1L;
        Long nodeId = 1L;
        String value = "value";

        // when
        redisService.save(chatId, nodeId, value);

        // then
        String key = chatId + ":" + nodeId;
        String storedValue = (String) redisTemplate.opsForValue().get(key);
        assertThat(storedValue).isEqualTo(value);
    }

    @DisplayName("Redis에 키로 저장된 값을 조회한다.")
    @Test
    void get() {
        // given
        Long chatId = 1L;
        Long nodeId = 1L;
        String key = chatId + ":" + nodeId;
        String value = "value";
        redisTemplate.opsForValue().set(key, value);

        // when
        String storedValue = redisService.get(chatId, nodeId);

        // then
        assertThat(storedValue).isEqualTo(value);
    }

    @DisplayName("Redis에 저장된 키가 있으면 True를 반환한다.")
    @Test
    void existsKey() {
        // given
        Long chatId = 1L;
        Long nodeId = 1L;
        String key = chatId + ":" + nodeId;
        String value = "value";
        redisTemplate.opsForValue().set(key, value);

        // when
        boolean result = redisService.exists(chatId, nodeId);

        // then
        assertTrue(result);
    }

    @DisplayName("chatId에 해당하는 모든 키를 삭제한다.")
    @Test
    void notExistsKey() {
        // given
        Long chatId = 1L;
        String value = "value";
        String key1 = chatId + ":" + 1;
        String key2 = chatId + ":" + 2;

        redisTemplate.opsForValue().set(key1, value);
        redisTemplate.opsForValue().set(key2, value);

        // when
        redisService.deleteAll(chatId);

        // then
        assertFalse(redisTemplate.hasKey(key1));
        assertFalse(redisTemplate.hasKey(key2));
    }

}
