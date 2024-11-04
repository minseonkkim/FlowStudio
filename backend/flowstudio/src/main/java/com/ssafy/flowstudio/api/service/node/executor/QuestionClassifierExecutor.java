package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.common.secret.SecretKeyProperties;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_3_5_TURBO;

@Component
public class QuestionClassifierExecutor extends NodeExecutor {

    private final SecretKeyProperties secretKeyProperties;

    public QuestionClassifierExecutor(RedisTemplate<String, Object> redisTemplate, SecretKeyProperties secretKeyProperties) {
        super(redisTemplate);
        this.secretKeyProperties = secretKeyProperties;
    }

    // TODO : model 데이터 넣기 + param_list 작성
    @Override
    public void execute(Node node, Chat chat) {
        QuestionClassifier questionClassifierNode = (QuestionClassifier) node;
        String classList = questionClassifierNode.getClassList();
        String modelParamList = questionClassifierNode.getModelParamList();
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(secretKeyProperties.getOpenAi())
                .modelName(GPT_3_5_TURBO)
                .build();

        SystemMessage systemMessage = new SystemMessage("system message");
        UserMessage userMessage = new UserMessage("user message");

        List<ChatMessage> messageList = new ArrayList<>();

        messageList.add(systemMessage);
        messageList.add(userMessage);


        System.out.println("QuestionClassifierExecutor");
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.QUESTION_CLASSIFIER;
    }
}
