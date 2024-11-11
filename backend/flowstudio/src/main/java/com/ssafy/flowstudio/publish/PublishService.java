package com.ssafy.flowstudio.publish;

import com.ssafy.flowstudio.api.service.chatflow.response.ChatFlowResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.node.entity.*;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublishService {

    private static final Logger log = LoggerFactory.getLogger(PublishService.class);
    @PersistenceUnit(unitName = "primary")
    private final EntityManagerFactory primaryEntityManagerFactory;

    @PersistenceUnit(unitName = "secondary")
    private final EntityManagerFactory secondaryEntityManagerFactory;

    private final ChatFlowRepository chatFlowRepository;
    private final PublishChatFlowRepository publishChatFlowRepository;
    private final EdgeRepository edgeRepository;
    private final NodeRepository nodeRepository;

    @Transactional(transactionManager = "multiTransactionManager")
    public ChatFlowResponse getPublishChatFlow(Long chatFlowId) {
        ChatFlow chatFlow = publishChatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        List<EdgeResponse> edges = edgeRepository.findByChatFlowId(chatFlowId).stream()
                .map(EdgeResponse::from)
                .toList();
        return ChatFlowResponse.from(chatFlow, edges);
    }

    public Long getChatId(String publishUrl) {
        ChatFlow chatFlow = publishChatFlowRepository.findByPublishUrl(publishUrl)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

        return chatFlow.getId();
    }

    @Transactional(transactionManager = "multiTransactionManager")
    public String publishChatFlow(Long chatFlowId) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        String publishUrl = chatFlow.getPublishUrl();
        if (publishUrl.isBlank()) {
            publishUrl = UUID.randomUUID().toString();
            chatFlow.updatePublishUrl(publishUrl);
            chatFlowRepository.save(chatFlow);
            chatFlowRepository.flush();
        }

        List<Node> nodes = nodeRepository.findByChatFlowId(chatFlow.getId());
        List<Edge> edges = edgeRepository.findByChatFlowId(chatFlow.getId());

        EntityManager em = secondaryEntityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        log.info("Using DataSource: {}", em.getEntityManagerFactory().getProperties().get("dbname"));

        // Author ApiKey
        em.createNativeQuery(
                        "INSERT INTO api_key (api_key_id, open_ai_key, claude_key, gemini_key, clova_key) VALUES (?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE open_ai_key = VALUES(open_ai_key), claude_key = VALUES(claude_key), " +
                                "gemini_key = VALUES(gemini_key), clova_key = VALUES(clova_key)")
                .setParameter(1, chatFlow.getAuthor().getApiKey().getId())
                .setParameter(2, chatFlow.getAuthor().getApiKey().getOpenAiKey())
                .setParameter(3, chatFlow.getAuthor().getApiKey().getClaudeKey())
                .setParameter(4, chatFlow.getAuthor().getApiKey().getGeminiKey())
                .setParameter(5, chatFlow.getAuthor().getApiKey().getClovaKey())
                .executeUpdate();

        // owner ApiKey
        em.createNativeQuery(
                        "INSERT INTO api_key (api_key_id, open_ai_key, claude_key, gemini_key, clova_key) VALUES (?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE open_ai_key = VALUES(open_ai_key), claude_key = VALUES(claude_key), " +
                                "gemini_key = VALUES(gemini_key), clova_key = VALUES(clova_key)")
                .setParameter(1, chatFlow.getOwner().getApiKey().getId())
                .setParameter(2, chatFlow.getOwner().getApiKey().getOpenAiKey())
                .setParameter(3, chatFlow.getOwner().getApiKey().getClaudeKey())
                .setParameter(4, chatFlow.getOwner().getApiKey().getGeminiKey())
                .setParameter(5, chatFlow.getOwner().getApiKey().getClovaKey())
                .executeUpdate();


        // Author User entity 복사
        em.createNativeQuery(
                        "INSERT INTO users (user_id, api_key_id, username, nickname, profile_image, provider_type, is_anonymous, created_at, updated_at) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE username = VALUES(username), nickname = VALUES(nickname), profile_image = VALUES(profile_image), provider_type = VALUES(provider_type), is_anonymous = VALUES(is_anonymous), updated_at = VALUES(updated_at)"
                )
                .setParameter(1, chatFlow.getAuthor().getId())
                .setParameter(2, chatFlow.getAuthor().getApiKey().getId())
                .setParameter(3, chatFlow.getAuthor().getUsername())
                .setParameter(4, chatFlow.getAuthor().getNickname())
                .setParameter(5, chatFlow.getAuthor().getProfileImage())
                .setParameter(6, chatFlow.getAuthor().getProviderType().name())
                .setParameter(7, chatFlow.getAuthor().isAnonymous())
                .setParameter(8, chatFlow.getAuthor().getCreatedAt())
                .setParameter(9, chatFlow.getAuthor().getUpdatedAt())
                .executeUpdate();

        // owner User entity 복사
        em.createNativeQuery(
                        "INSERT INTO users (user_id, api_key_id, username, nickname, profile_image, provider_type, is_anonymous, created_at, updated_at) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE username = VALUES(username), nickname = VALUES(nickname), profile_image = VALUES(profile_image), provider_type = VALUES(provider_type), is_anonymous = VALUES(is_anonymous), updated_at = VALUES(updated_at)"
                )
                .setParameter(1, chatFlow.getOwner().getId())
                .setParameter(2, chatFlow.getOwner().getApiKey().getId())
                .setParameter(3, chatFlow.getOwner().getUsername())
                .setParameter(4, chatFlow.getOwner().getNickname())
                .setParameter(5, chatFlow.getOwner().getProfileImage())
                .setParameter(6, chatFlow.getOwner().getProviderType().name())
                .setParameter(7, chatFlow.getOwner().isAnonymous())
                .setParameter(8, chatFlow.getOwner().getCreatedAt())
                .setParameter(9, chatFlow.getOwner().getUpdatedAt())
                .executeUpdate();


        // ChatFlow
        em.createNativeQuery(
                        "INSERT INTO chat_flow (chat_flow_id, owner_id, author_id, title, description, thumbnail, is_public, share_count, publish_url, created_at, updated_at) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE title = VALUES(title), description = VALUES(description), thumbnail = VALUES(thumbnail), is_public = VALUES(is_public), share_count = VALUES(share_count), publish_url = VALUES(publish_url), updated_at = VALUES(updated_at)"
                )
                .setParameter(1, chatFlow.getId())
                .setParameter(2, chatFlow.getOwner().getId())
                .setParameter(3, chatFlow.getAuthor().getId())
                .setParameter(4, chatFlow.getTitle())
                .setParameter(5, chatFlow.getDescription())
                .setParameter(6, chatFlow.getThumbnail())
                .setParameter(7, chatFlow.isPublic())
                .setParameter(8, chatFlow.getShareCount())
                .setParameter(9, chatFlow.getPublishUrl())
                .setParameter(10, chatFlow.getCreatedAt())
                .setParameter(11, chatFlow.getUpdatedAt())
                .executeUpdate();
        log.info("uuid : {}",chatFlow.getPublishUrl());


        for (Node node : nodes) {
            // 공통 Node 필드 추가
            em.createNativeQuery(
                            "INSERT INTO node (node_id, chat_flow_id, name, type, x, y, created_at, updated_at) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                                    "ON DUPLICATE KEY UPDATE name = VALUES(name), type = VALUES(type), x = VALUES(x), y = VALUES(y), updated_at = VALUES(updated_at)"
                    )
                    .setParameter(1, node.getId())
                    .setParameter(2, node.getChatFlow().getId())
                    .setParameter(3, node.getName())
                    .setParameter(4, node.getType().name())
                    .setParameter(5, node.getCoordinate().getX())
                    .setParameter(6, node.getCoordinate().getY())
                    .setParameter(7, node.getCreatedAt())
                    .setParameter(8, node.getUpdatedAt())
                    .executeUpdate();

            // 타입별 추가 필드 추가
            switch (node.getType()) {
                case START -> {
                    Start startNode = (Start) node;
                    em.createNativeQuery(
                                    "INSERT INTO start (node_id, max_length) VALUES (?, ?) " +
                                            "ON DUPLICATE KEY UPDATE max_length = VALUES(max_length)"
                            )
                            .setParameter(1, startNode.getId())
                            .setParameter(2, startNode.getMaxLength())
                            .executeUpdate();
                }
                case LLM -> {
                    LLM llmNode = (LLM) node;
                    em.createNativeQuery(
                                    "INSERT INTO llm (node_id, prompt_system, prompt_user, context, temperature, max_tokens, model_provider, model_name) " +
                                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                                            "ON DUPLICATE KEY UPDATE prompt_system = VALUES(prompt_system), prompt_user = VALUES(prompt_user), " +
                                            "context = VALUES(context), temperature = VALUES(temperature), max_tokens = VALUES(max_tokens), " +
                                            "model_provider = VALUES(model_provider), model_name = VALUES(model_name)"
                            )
                            .setParameter(1, llmNode.getId())
                            .setParameter(2, llmNode.getPromptSystem())
                            .setParameter(3, llmNode.getPromptUser())
                            .setParameter(4, llmNode.getContext())
                            .setParameter(5, llmNode.getTemperature())
                            .setParameter(6, llmNode.getMaxTokens())
                            .setParameter(7, llmNode.getModelProvider().name())
                            .setParameter(8, llmNode.getModelName().name())
                            .executeUpdate();
                }
                case RETRIEVER -> {
                    Retriever retrieverNode = (Retriever) node;

                    // Knowledge
                    Knowledge knowledge = retrieverNode.getKnowledge();
                    em.createNativeQuery(
                                    "INSERT INTO knowledge (knowledge_id, user_id, title, is_public, total_token, created_at, updated_at) " +
                                            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                                            "ON DUPLICATE KEY UPDATE title = VALUES(title), is_public = VALUES(is_public), total_token = VALUES(total_token), updated_at = VALUES(updated_at)"
                            )
                            .setParameter(1, knowledge.getId())
                            .setParameter(2, knowledge.getUser().getId())
                            .setParameter(3, knowledge.getTitle())
                            .setParameter(4, knowledge.isPublic())
                            .setParameter(5, knowledge.getTotalToken())
                            .setParameter(6, knowledge.getCreatedAt())
                            .setParameter(7, knowledge.getUpdatedAt())
                            .executeUpdate();

                    // Retriever
                    em.createNativeQuery(
                                    "INSERT INTO retriever (node_id, knowledge_id, interval_time, topk, score_threshold, query) " +
                                            "VALUES (?, ?, ?, ?, ?, ?) " +
                                            "ON DUPLICATE KEY UPDATE interval_time = VALUES(interval_time), topk = VALUES(topk), " +
                                            "score_threshold = VALUES(score_threshold), query = VALUES(query)"
                            )
                            .setParameter(1, retrieverNode.getId())
                            .setParameter(2, retrieverNode.getKnowledge() != null ? retrieverNode.getKnowledge().getId() : null)
                            .setParameter(3, retrieverNode.getIntervalTime())
                            .setParameter(4, retrieverNode.getTopK())
                            .setParameter(5, retrieverNode.getScoreThreshold())
                            .setParameter(6, retrieverNode.getQuery())
                            .executeUpdate();
                }
                case QUESTION_CLASSIFIER -> {
                    QuestionClassifier questionClassifierNode = (QuestionClassifier) node;

                    em.createNativeQuery(
                                    "INSERT INTO question_classifier (node_id) VALUES (?) " +
                                            "ON DUPLICATE KEY UPDATE node_id = VALUES(node_id)"
                            )
                            .setParameter(1, questionClassifierNode.getId())
                            .executeUpdate();
                    // QuestionClass
                    for (QuestionClass questionClass : questionClassifierNode.getQuestionClasses()) {
                        em.createNativeQuery(
                                        "INSERT INTO question_class (question_class_id, content, question_classifier_id) " +
                                                "VALUES (?, ?, ?) " +
                                                "ON DUPLICATE KEY UPDATE content = VALUES(content), question_classifier_id = VALUES(question_classifier_id)"
                                )
                                .setParameter(1, questionClass.getId())
                                .setParameter(2, questionClass.getContent())
                                .setParameter(3, questionClassifierNode.getId())
                                .executeUpdate();
                    }

                }
                case ANSWER -> {
                    Answer answerNode = (Answer) node;
                    em.createNativeQuery(
                                    "INSERT INTO answer (node_id, output_message) VALUES (?, ?) " +
                                            "ON DUPLICATE KEY UPDATE output_message = VALUES(output_message)"
                            )
                            .setParameter(1, answerNode.getId())
                            .setParameter(2, answerNode.getOutputMessage())
                            .executeUpdate();
                }
                default -> throw new IllegalArgumentException("Unknown node type: " + node.getType());
            }
        }

        // Edge entity 복사
        for (Edge edge : edges) {
            em.createNativeQuery(
                            "INSERT INTO edge (edge_id, source_node_id, target_node_id, source_condition_id, created_at, updated_at) " +
                                    "VALUES (?, ?, ?, ?, ?, ?) " +
                                    "ON DUPLICATE KEY UPDATE source_node_id = VALUES(source_node_id), target_node_id = VALUES(target_node_id), source_condition_id = VALUES(source_condition_id), updated_at = VALUES(updated_at)"
                    )
                    .setParameter(1, edge.getId())
                    .setParameter(2, edge.getSourceNode().getId())
                    .setParameter(3, edge.getTargetNode().getId())
                    .setParameter(4, edge.getSourceConditionId())
                    .setParameter(5, edge.getCreatedAt())
                    .setParameter(6, edge.getUpdatedAt())
                    .executeUpdate();
        }


        em.getTransaction().commit();
        em.close();

        return publishUrl;
    }
}
