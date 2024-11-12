package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
class LlmExecutorTest extends IntegrationTestSupport {

    @Autowired
    private LlmExecutor llmExecutor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @DisplayName("LLM 노드 실행")
    @Test
    void execute() {
        // given
        ApiKey apiKey = ApiKey.builder()
                .openAiKey("openAiKey")
                .claudeKey("claudeKey")
                .build();

        User user = User.builder()
                .username("test")
                .apiKey(apiKey)
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("test")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(0)
                .y(0)
                .build();

        String promptSystem = """
            아래 정보를 바탕으로 대답해줘
            참새의 "참"은 원래 ㅏ가 아니라 아래아가 모음으로 들어 있었다 하며, 그 뜻은 '좀새', 즉 '작은 새'였다는 설이 있다.[3] 다만 '규합총서(閨閤叢書)' 등에서 참새를 '진쵸(眞隹, 혹은 眞鳥)'로 적는 등, 예부터 '참새'의 뜻을 '그냥 새', '흔한 새' 등으로 여기는 경우도 많았던 것으로 보인다.
            
            영어로는 스패로우(sparrow)라 불린다. 하지만 실제로 서양인들이 스패로우라 부르는 새를 보면 한국의 참새와는 딴판인 새가 섞여 있다. 특히 미국참새는 참새과가 아닌 신대륙멧새과에 속하며 한국의 참새와는 과 단위에서 다른 종이다. 물론 미국에도 한국의 참새와 같은 참새과에 속하는 집참새(House sparrow, Passer domesticus)도 있다. [4] 미국인은 한국의 참새를 "독일 참새" 또는 "유라시아 참새"라 부르고 미국참새를 참새(스패로우)라 부른다. 스패로우란 명칭은 옛 앵글로색슨어인 spearwa(스패아와)에서 유래한 것인데, 이는 '파닥파닥'이란 뜻으로, 날개를 바쁘게 치며 날아다니는 작은 새를 뭉뚱그려 지칭하는 일반명사였다 한다.
            
            유럽 대륙에도 한국의 참새와 같은 참새가 있는데, 유럽의 참새는 한국의 참새처럼 인간 마을에서 사는 게 아니라 주로 산이나 들에서 살아간다. 반면 한중일 등 아시아 참새들은 마을이나 도시에서 흔히 볼 수 있다.
            
            몸길이는 평균 12~13cm 정도 된다. 비록 참새가 작은 새라고 해도 왕사마귀나 장수잠자리, 방아깨비같은 대형급 곤충들보다는 확실히 거대하다.
        """;
        String promptUser = "참새에 대해 설명해줘";

        Node llmNode = LLM.builder()
                .id(1L)
                .chatFlow(chatFlow)
                .name("LLM")
                .type(NodeType.LLM)
                .coordinate(coordinate)
                .promptSystem(promptSystem)
                .promptUser(promptUser)
                .temperature(1.0)
                .maxTokens(512)
                .modelProvider(ModelProvider.OPENAI)
                .modelName(ModelName.GPT_4_O_MINI)
                .build();

        Chat chat = Chat.builder()
                .id(77L)
                .isPreview(true)
                .messageList("[]")
                .chatFlow(chatFlow)
                .user(user)
                .build();

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        llmExecutor.execute(llmNode, chat);

        // then


    }
}
