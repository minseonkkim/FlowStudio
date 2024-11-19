package com.ssafy.flowstudio.common.util;

import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class MessageParseUtilTest extends IntegrationTestSupport {

    @Autowired
    private MessageParseUtil messageParseUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.delete("*");
    }

    @DisplayName("")
    @Test
    void replace() {
        // given
        Long chatId = 1L;
        String message = "example {{1}} example {{input-message}} word";

        redisTemplate.opsForValue().set(chatId + ":" + 1, "message1");
        redisTemplate.opsForValue().set(chatId + ":" + "input-message", "message2");

        // when
        String result = messageParseUtil.replace(message, chatId);

        // then
        assertThat(result).isEqualTo("example message1 example message2 word");
    }
}