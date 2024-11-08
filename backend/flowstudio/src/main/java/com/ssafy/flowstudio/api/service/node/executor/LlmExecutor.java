package com.ssafy.flowstudio.api.service.node.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.api.service.node.event.NodeEvent;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.util.MessageParseUtil;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLog;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLogRepository;
import dev.ai4j.openai4j.OpenAiHttpException;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LlmExecutor extends NodeExecutor {

    private final RedisService redisService;
    private final TokenUsageLogRepository tokenUsageLogRepository;
    private final ChatRepository chatRepository;
    private final ChatModelFactory chatModelFactory;
    private final MessageParseUtil messageParseUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LlmExecutor(RedisService redisService, ApplicationEventPublisher eventPublisher, TokenUsageLogRepository tokenUsageLogRepository, ChatRepository chatRepository, ChatModelFactory chatModelFactory, MessageParseUtil messageParseUtil) {
        super(redisService, eventPublisher);
        this.tokenUsageLogRepository = tokenUsageLogRepository;
        this.chatRepository = chatRepository;
        this.redisService = redisService;
        this.chatModelFactory = chatModelFactory;
        this.messageParseUtil = messageParseUtil;
    }

    @Override
    public void execute(Node node, Chat chat) {
        LLM llmNode = (LLM) node;

        // 프롬프트 완성
        String promptSystem = messageParseUtil.replace(llmNode.getPromptSystem(), chat.getId());
        String promptUser = messageParseUtil.replace(llmNode.getPromptUser(), chat.getId());

        // 모델에게 보낼 메시지 생성
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(new SystemMessage(promptSystem));
        messageList.add(new UserMessage(promptUser));

        try {
            // 챗 모델 생성
            ChatLanguageModel chatModel = chatModelFactory.createChatModel(chat, llmNode);

            // 결과 반환
            Response<AiMessage> response = chatModel.generate(messageList);
            String LlmOutputMessage = response.content().text();

            // 레디스에 결과 저장
            redisService.save(chat.getId(), node.getId(), LlmOutputMessage);

            // 배포환경 추가 작업
            if (!chat.isPreview()) {
                // 토큰 사용로그 기록
                Integer tokenUsage = response.tokenUsage().totalTokenCount();
                tokenUsageLogRepository.save(TokenUsageLog.create(chat.getUser(), tokenUsage));

                // 챗 히스토리 업데이트
                updateChatHistory(chat, promptUser, LlmOutputMessage);
            }

        } catch (OpenAiHttpException e) {
            throw new BaseException(ErrorCode.API_KEY_INVALID);
        }

        // 다음 노드가 있으면 실행
        if (!node.getOutputEdges().isEmpty()) {
            Node targetNode = node.getOutputEdges().get(0).getTargetNode();
            publishEvent(NodeEvent.of(this, targetNode, chat));
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
        return NodeType.LLM;
    }
}
