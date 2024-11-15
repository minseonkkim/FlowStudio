package com.ssafy.flowstudio.api.service.chatflowtest;

import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestListResponse;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.chatflowtest.ChatFlowTestRepository;
import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
class ChatFlowTestServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChatFlowTestService chatFlowTestService;

    @Autowired
    private ChatFlowTestRepository chatFlowTestRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private UserRepository userRepository;



    @DisplayName("챗플로우의 테스트 목록을 조회한다")
    @Test
    void getChatFlowTests() {
        // given
        User user = User.builder()
                .username("test")
                .nickname("test")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("test")
                .build();

        ChatFlowTest chatFlowTest1 = ChatFlowTest.builder()
                .user(user)
                .chatFlow(chatFlow)
                .embeddingDistanceMean(1.0f)
                .embeddingDistanceVariance(0.1f)
                .crossEncoderMean(0.9f)
                .crossEncoderVariance(0.2f)
                .rougeMetricMean(0.8f)
                .rougeMetricVariance(0.3f)
                .totalTestCount(10)
                .build();

        ChatFlowTest chatFlowTest2 = ChatFlowTest.builder()
                .user(user)
                .chatFlow(chatFlow)
                .embeddingDistanceMean(1.0f)
                .embeddingDistanceVariance(0.1f)
                .crossEncoderMean(0.9f)
                .crossEncoderVariance(0.2f)
                .rougeMetricMean(0.8f)
                .rougeMetricVariance(0.3f)
                .totalTestCount(10)
                .build();

        chatFlow.getTests().add(chatFlowTest1);
        chatFlow.getTests().add(chatFlowTest2);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        List<ChatFlowTestListResponse> response = chatFlowTestService.getChatFlowTests(user, chatFlow.getId());

        // then
        assertThat(response).hasSize(2)
                .extracting("embeddingDistanceMean", "embeddingDistanceVariance", "crossEncoderMean", "crossEncoderVariance", "rougeMetricMean", "rougeMetricVariance")
                .containsExactlyInAnyOrder(
                        tuple(1.0f, 0.1f, 0.9f, 0.2f, 0.8f, 0.3f),
                        tuple(1.0f, 0.1f, 0.9f, 0.2f, 0.8f, 0.3f)
                );
    }

}