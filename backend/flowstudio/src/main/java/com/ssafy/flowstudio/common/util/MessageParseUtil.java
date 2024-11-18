package com.ssafy.flowstudio.common.util;

import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.domain.node.entity.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
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
            String newValue = redisService.get(key);
            if (newValue != null) {
                result.append(newValue); // {{}} 대신 키값 추가
            } else {
                result.append(matcher.group(0)); // Redis에서 해당 키와 매칭되는 값이 없으면 그대로 다시 넣어준다.
            }
            lastEnd = matcher.end(); // 현재 매칭 끝 지점 업데이트
        }
        result.append(message.substring(lastEnd)); // 남은 부분 추가

        return result.toString();
    }

    // 챗플로우 복제 시 프롬프트 내의 변수 값을 복제된 노드의 ID로 매핑한다.
    public String replace(String message, Map<Long, Node> nodeMap) {

        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(message);

        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            String originalVariable =  matcher.group(1);
            result.append(message, lastEnd, matcher.start()); // Matcher 이전의 문자를 추가한다.
            // 원본 프롬프트 내의 변수가 숫자값일때는 노드의 ID이므로 복제된 노드의 ID로 새로 매핑한다.
            if (originalVariable.matches("\\d+") && nodeMap.get(Long.parseLong(originalVariable)) != null) {
                Node clonedNode = nodeMap.get(Long.parseLong(originalVariable));
                result.append("{{" + clonedNode.getId() + "}}");
            } else {
                // ChatEnvVariable일 경우, 그리고 그 외의 모든 경우에는 교체 없이 그대로 다시 넣어준다.
                result.append(matcher.group(0));
            }
            lastEnd = matcher.end(); // 현재 매칭 끝 지점을 업데이트한다.
        }
        result.append(message.substring(lastEnd)); // 남은 부분 추가

        return result.toString();
    }

}
