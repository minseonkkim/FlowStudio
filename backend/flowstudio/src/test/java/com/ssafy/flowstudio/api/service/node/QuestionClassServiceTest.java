package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.controller.node.request.QuestionClassCreateRequest;
import com.ssafy.flowstudio.api.controller.node.request.QuestionClassUpdateRequest;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassResponse;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.node.entity.Start;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.node.repository.QuestionClassRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@ActiveProfiles("test")
class QuestionClassServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private QuestionClassService questionClassService;

    @Autowired
    private QuestionClassRepository questionClassRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @DisplayName("질문 분류를 생성한다.")
    @Test
    void createQuestionClass() {
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

        QuestionClassCreateRequest questionClassCreateRequest = QuestionClassCreateRequest.builder()
                .content("question-content")
                .build();

        // when
        QuestionClassResponse questionClassResponse = questionClassService.createQuestionClass(questionClassifier.getId());

        // then
        assertThat(questionClassResponse).isNotNull()
                .extracting("content", "questionClassifierId")
                .containsExactly(null, questionClassifier.getId());

    }

    @DisplayName("질문 분류의 내용을 수정한다.")
    @Test
    void updateQuestionClassContent() {
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

        QuestionClass questionClass = QuestionClass.empty();
        questionClass.updateQuestionClassifier(questionClassifier);
        questionClassRepository.save(questionClass);

        QuestionClassUpdateRequest questionClassUpdateRequest = QuestionClassUpdateRequest.builder()
                .content("updated-question-content")
                .build();

        // when
        QuestionClassResponse questionClassResponse = questionClassService.updateQuestionClass(questionClass.getId(), questionClassUpdateRequest.toServiceRequest());

        // then
        assertThat(questionClassResponse).isNotNull()
                .extracting("id", "content", "questionClassifierId")
                .containsExactly(questionClass.getId(), "updated-question-content", questionClassifier.getId());
    }

    @DisplayName("질문 분류를 삭제한다.")
    @Test
    void deleteQuestionClassContent() {
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

        Start start = Start.create(chatFlow, coordinate);
        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, coordinate);
        chatFlow.addNode(questionClassifier);
        chatFlow.addNode(start);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        QuestionClass questionClass = QuestionClass.empty();
        questionClass.updateQuestionClassifier(questionClassifier);
        questionClassRepository.save(questionClass);

        Edge edge = Edge.builder()
                .sourceNode(start)
                .targetNode(questionClassifier)
                .sourceConditionId(questionClass.getId())
                .build();

        edgeRepository.save(edge);

        // when
        boolean result = questionClassService.deleteQuestionClass(questionClass.getId());

        // then
        assertThat(result).isTrue();
        assertThat(edgeRepository.existsById(edge.getId())).isFalse();
    }

}
