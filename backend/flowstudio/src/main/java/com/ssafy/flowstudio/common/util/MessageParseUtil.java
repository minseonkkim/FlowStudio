package com.ssafy.flowstudio.common.util;

import com.ssafy.flowstudio.api.service.node.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class MessageParseUtil {

    private final RedisService redisService;

    public String replace(String message, Long chatId) {

        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(message);

        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            String key =  chatId + ":" + matcher.group(1);
            result.append(message, lastEnd, matcher.start()); // 이전 문자 추가
            // TODO: 키가 없을 때 예외처리
            result.append(redisService.get(key)); // {{}} 대신 키값 추가
            lastEnd = matcher.end(); // 현재 매칭 끝 지점 업데이트
        }
        result.append(message.substring(lastEnd)); // 남은 부분 추가

        return result.toString();
    }

}
