package com.ssafy.flowstudio.api.service.node.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LlmExecutor extends NodeExecutor {

    private final RedisService redisService;
    private final TokenUsageLogRepository tokenUsageLogRepository;
    private final ChatModelFactory chatModelFactory;
    private final MessageParseUtil messageParseUtil;
    private final ChatTitleMaker chatTitleMaker;
    private static final Logger log = LoggerFactory.getLogger(LlmExecutor.class);

    public LlmExecutor(RedisService redisService, ApplicationEventPublisher eventPublisher, TokenUsageLogRepository tokenUsageLogRepository, ChatRepository chatRepository, ChatModelFactory chatModelFactory, MessageParseUtil messageParseUtil, SseEmitters sseEmitters, ChatTitleMaker chatTitleMaker) {
        super(redisService, eventPublisher, sseEmitters);
        this.tokenUsageLogRepository = tokenUsageLogRepository;
        this.redisService = redisService;
        this.chatModelFactory = chatModelFactory;
        this.messageParseUtil = messageParseUtil;
        this.chatTitleMaker = chatTitleMaker;
    }

    @Override
    public void execute(Node node, Chat chat) {
        LLM llmNode = (LLM) node;

        // 유저 프롬프트가 비어있으면 예외 발생
        if (llmNode.getPromptUser() == null || llmNode.getPromptUser().trim().isEmpty()) {
            throw new BaseException(ErrorCode.REQUIRED_NODE_VALUE_NOT_EXIST);
        }

        // 유저 프롬프트 파싱
        String promptUser = messageParseUtil.replace(llmNode.getPromptUser(), chat.getId());

        // 모델에게 보낼 메시지 생성
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(new UserMessage(promptUser));

        // 시스템 프롬프트가 빈 값이 아닐때만 파싱 후 추가
        if (llmNode.getPromptSystem() != null && !llmNode.getPromptSystem().trim().isEmpty()) {
            String promptSystem = messageParseUtil.replace(llmNode.getPromptSystem(), chat.getId());
            messageList.add(new SystemMessage(promptSystem));
        }

        try {
            // 챗 모델 생성
            ChatLanguageModel chatModel = chatModelFactory.createChatModel(chat, llmNode);

            // 결과 반환
            Response<AiMessage> response = chatModel.generate(messageList);
            String llmOutputMessage = response.content().text();

            // 레디스에 결과 저장
            redisService.save(chat.getId(), node.getId(), llmOutputMessage);

            // 결과 SSE로 전송
            sseEmitters.send(chat.getUser(), llmNode, llmOutputMessage);

            if (chat.getMessageList().equals("[]") && !chat.isPreview()) {
                chatTitleMaker.makeTitle(chat, chatModel, promptUser);
            }

            if (!chat.isPreview()) {
                // 토큰 사용로그 기록
                Integer tokenUsage = response.tokenUsage().totalTokenCount();
                tokenUsageLogRepository.save(TokenUsageLog.create(chat.getUser(), tokenUsage));
            }
        } catch (OpenAiHttpException e) {
            log.error("API_KEY_INVALID: ", e);
            throw new BaseException(ErrorCode.API_KEY_INVALID);
        }

        // 다음 노드가 있으면 실행
        if (!node.getOutputEdges().isEmpty()) {
            Node targetNode = node.getOutputEdges().get(0).getTargetNode();
            publishEvent(NodeEvent.of(this, targetNode, chat));
        }
    }


    @Override
    public NodeType getNodeType() {
        return NodeType.LLM;
    }

}
