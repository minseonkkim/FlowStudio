package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
class QuestionClassRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private QuestionClassRepository questionClassRepository;

    @DisplayName("특정 챗플로우에서 사용하는 지식 분류를 조회한다.")
    @Test
    void findByChatFlowId() {
        // given
        User user = User.builder()
                .username("test")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .owner(user)
                .author(user)
                .title("title")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .x(1)
                .y(1)
                .build();

        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, coordinate);
        chatFlow.addNode(questionClassifier);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        QuestionClass questionClass1 = QuestionClass.empty();
        questionClass1.updateQuestionClassifier(questionClassifier);

        QuestionClass questionClass2 = QuestionClass.empty();
        questionClass2.updateQuestionClassifier(questionClassifier);

        questionClassRepository.save(questionClass1);
        questionClassRepository.save(questionClass2);

        // when
        List<QuestionClass> foundQuestionClasses = questionClassRepository.findByChatFlowId(chatFlow.getId());

        // then
        assertThat(foundQuestionClasses)
                .isNotEmpty()
                .hasSize(2);

        assertThat(foundQuestionClasses.get(0).getContent()).isNull();
        assertThat(foundQuestionClasses.get(0).getQuestionClassifier().getId()).isEqualTo(questionClassifier.getId());

    }
}