package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
class QuestionClassifierExecutorTest extends IntegrationTestSupport {

    @Mock
    private ApplicationEventPublisher publisher;

    @Autowired
    private QuestionClassifierExecutor questionClassifierExecutor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @Autowired
    private RedisService redisService;

    @DisplayName("질문 분류기 노드를 실행한다.")
    @Test
    void execute() {
        // given
//        User user = User.builder()
//                .username("test")
//                .build();
//
//        ChatFlow chatFlow = ChatFlow.builder()
//                .owner(user)
//                .author(user)
//                .title("title")
//                .build();
//
//        Coordinate coordinate = Coordinate.builder()
//                .x(1)
//                .y(1)
//                .build();
//
//        QuestionClassifier questionClassifier = QuestionClassifier.create(chatFlow, coordinate);
//
//        QuestionClass questionClass1 = QuestionClass.create("한국");
//        QuestionClass questionClass2 = QuestionClass.create("중국");
//        QuestionClass questionClass3 = QuestionClass.create("일본");
//
//        questionClass1.updateQuestionClassifier(questionClassifier);
//        questionClass2.updateQuestionClassifier(questionClassifier);
//        questionClass3.updateQuestionClassifier(questionClassifier);
//
//        Answer answer1 = Answer.create(chatFlow, coordinate);
//        Answer answer2 = Answer.create(chatFlow, coordinate);
//        Answer answer3 = Answer.create(chatFlow, coordinate);
//
//        answer1.updateOutputMessage("한국에 대한 답변");
//        answer2.updateOutputMessage("중국에 대한 답변");
//        answer3.updateOutputMessage("일본에 대한 답변");
//
//        Edge edge1 = Edge.create(questionClassifier, answer1, questionClass1.getId());
//        Edge edge2 = Edge.create(questionClassifier, answer2, questionClass2.getId());
//        Edge edge3 = Edge.create(questionClassifier, answer3, questionClass3.getId());
//
//        questionClass1.update(edge1, "한국");
//        questionClass2.update(edge2, "중국");
//        questionClass3.update(edge3, "일본");
//
//        chatFlow.addNode(questionClassifier);
//        chatFlow.addNode(answer1);
//        chatFlow.addNode(answer2);
//        chatFlow.addNode(answer3);
//
//        userRepository.save(user);
//        chatFlowRepository.save(chatFlow);
//
//        Chat chat = Chat.create(user, chatFlow);
//
//        redisService.save(chat.getId(), ChatEnvVariable.INPUT_MESSAGE, "스시에 대해 알려줘");
//
//        // when
//        questionClassifierExecutor.execute(questionClassifier, chat);
    }
}