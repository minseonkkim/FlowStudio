package com.ssafy.flowstudio.api.service.node.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.api.service.chatflowtest.event.ChatFlowTestEvent;
import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.secret.SecretKeyProperties;
import com.ssafy.flowstudio.common.util.MessageParseUtil;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.ModelName;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
public class AnswerExecutor extends NodeExecutor {

    private final MessageParseUtil messageParseUtil;
    private final ChatRepository chatRepository;
    private final ChatTitleMaker chatTitleMaker;
    private final SecretKeyProperties secretKeyProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AnswerExecutor(RedisService redisService, ApplicationEventPublisher eventPublisher, MessageParseUtil messageParseUtil, SseEmitters sseEmitters, ChatRepository chatRepository, ChatTitleMaker chatTitleMaker, SecretKeyProperties secretKeyProperties) {
        super(redisService, eventPublisher, sseEmitters);
        this.messageParseUtil = messageParseUtil;
        this.chatRepository = chatRepository;
        this.chatTitleMaker = chatTitleMaker;
        this.secretKeyProperties = secretKeyProperties;
    }

    @Override
    public void execute(Node node, Chat chat) {
        Answer answerNode = (Answer) node;

        // 사용자가 변수와 함께 등록한 Output Message를 파싱한다.
        String outputMessage = answerNode.getOutputMessage();

        // Answer 노드는 Output Message를 필수로 필요로 한다.
        if (outputMessage == null || outputMessage.trim().isEmpty()) {
            throw new BaseException(ErrorCode.REQUIRED_NODE_VALUE_NOT_EXIST);
        }

        String answerOutput = messageParseUtil.replace(outputMessage, chat.getId());

        // 완성된 메시지를 SSE를 통해 클라이언트에게 전송한다.
        sseEmitters.send(chat.getUser(), answerNode, answerOutput);

        String inputMessage = redisService.get(chat.getId() + ":" + ChatEnvVariable.INPUT_MESSAGE);

        if (chat.getMessageList().equals("[]") && !chat.isPreview()) {
            ChatLanguageModel chatModel = OpenAiChatModel.builder()
                    .apiKey(secretKeyProperties.getOpenAi())
                    .modelName(ModelName.GPT_4_O_MINI.getName())
                    .temperature(0.3)
                    .maxTokens(512)
                    .build();

            chatTitleMaker.makeTitle(chat, chatModel, inputMessage);
        }

        updateChatHistory(chat, inputMessage, answerOutput);

        if(chat.isTest()) {
            redisService.saveTestValue(chat.getId(), answerOutput);
            sseEmitters.sendChatFlowTestLlm(chat, answerOutput);
            ChatFlowTestEvent event = ChatFlowTestEvent.of(this, chat);
            publishEvent(event);
        }

    }

    private void updateChatHistory(Chat chat, String promptUser, String LlmOutputMessage) {
        try {
            String chatHistory = chat.getMessageList();
            // 문자열을 JSON 객체로 변환
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(chatHistory);

            // 새 JSON 객체 생성
            ObjectNode newObject = objectMapper.createObjectNode();
            newObject.put("question", promptUser);
            newObject.put("answer", LlmOutputMessage);

            // 새 객체를 JSON 배열에 추가
            arrayNode.add(newObject);

            // JSON 배열을 문자열로 변환
            String updatedChatHistory = objectMapper.writeValueAsString(arrayNode);

            // 채팅 기록 업데이트
            chat.updateHistory(updatedChatHistory);
            chatRepository.save(chat);

        } catch (Exception e) {
            throw new IllegalArgumentException("Chat history update failed");
        }
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ANSWER;
    }
}
