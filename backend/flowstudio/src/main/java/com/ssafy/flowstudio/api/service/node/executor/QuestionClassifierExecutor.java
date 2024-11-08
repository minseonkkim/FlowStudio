package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.api.service.node.event.NodeEvent;
import com.ssafy.flowstudio.api.service.node.executor.prompt.QuestionClassifierPrompt;
import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.secret.SecretKeyProperties;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.node.entity.*;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import groovy.util.logging.Slf4j;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_3_5_TURBO;

@Slf4j
@Component
public class QuestionClassifierExecutor extends NodeExecutor {

    private static final Logger log = LoggerFactory.getLogger(QuestionClassifierExecutor.class);
    private final SecretKeyProperties secretKeyProperties;
    private final ApplicationEventPublisher eventPublisher;

    public QuestionClassifierExecutor(RedisService redisService, SecretKeyProperties secretKeyProperties, ApplicationEventPublisher eventPublisher) {
        super(redisService);
        this.secretKeyProperties = secretKeyProperties;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(Node node, Chat chat) {
        // 질문 분류기 노드를 참조하는 질문 분류(QuestionClass)들의 리스트를 불러온다.
        QuestionClassifier questionClassifierNode = (QuestionClassifier) node;
        List<QuestionClass> questionClasses = questionClassifierNode.getQuestionClasses();

        // GPT 모델을 빌드한다.
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(secretKeyProperties.getOpenAi())
                .modelName(GPT_3_5_TURBO)
                .build();

        // Redis로부터 해당 chat의 유저 입력 메시지를 가져온다.
        String inputMessageValue = String.valueOf(redisService.get(chat.getId(), ChatEnvVariable.INPUT_MESSAGE));

        // System Message와 User Message를 빌드한다.
        SystemMessage systemMessage = new SystemMessage(QuestionClassifierPrompt.systemMessage);

        String customUserMessage = CustomUserMessage.builder()
                .inputText(inputMessageValue)
                .questionClasses(questionClasses)
                .classificationInstruction(QuestionClassifierPrompt.classificationInstruction)
                .build()
                .toString();

        UserMessage userMessage = new UserMessage(customUserMessage);

        // 빌드된 메시지들을 List에 넣는다
        List<ChatMessage> messageList = new ArrayList<>();

        messageList.add(systemMessage);
        messageList.add(userMessage);

        // 메시지의 List를 넣어서 AI로 요청을 보낸 후 응답을 받는다.
        Response<AiMessage> response = model.generate(messageList);
        String responseText = response.content().text();

        try {
            // AI의 답변을 QuestionClass의 ID로 변환한다.
            Long foundId = Long.parseLong(responseText);

            log.info("AI response: " + responseText);

            // AI가 반환한 ID로 QuestionClass를 찾는다.
            QuestionClass chosenQuestionClass = questionClasses.stream()
                    .filter(qc -> qc.getId() == foundId)
                    .findFirst()
                    .orElseThrow(() -> new BaseException(ErrorCode.AI_RESPONSE_NOT_MATCH_GIVEN_CONDITION));

            // QuestionClass와 연결된 간선과 타겟 노드를 가져온다.
            Edge edge = chosenQuestionClass.getEdge();
            Node targetNode = edge.getTargetNode();

            // 타겟 노드와 chat 정보를 담은 Event를 생성한다.
            NodeEvent event = new NodeEvent(this, targetNode, chat);

            // event를 발행한다.
            eventPublisher.publishEvent(event);

            // Redis에 Output을 업데이트한다.
            redisService.save(chat.getId(), questionClassifierNode.getId(), chosenQuestionClass.getContent());

        } catch (NumberFormatException e) {
            throw new BaseException(ErrorCode.AI_RESPONSE_NOT_MATCH_GIVEN_SCHEMA);
        }
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.QUESTION_CLASSIFIER;
    }

    @Builder
    static class CustomUserMessage {
        private final String inputText;
        private final List<QuestionClass> questionClasses;
        private final String classificationInstruction;

        @Override
        public String toString() {
            return "{" +
                    "inputText='" + inputText + '\'' +
                    ", questionClasses=" + questionClasses +
                    ", classificationInstruction='" + classificationInstruction + '\'' +
                    '}';
        }
    }
}
