package com.ssafy.flowstudio.api.service.node.executor;

import com.ssafy.flowstudio.api.controller.sse.SseEmitters;
import com.ssafy.flowstudio.api.service.node.RedisService;
import com.ssafy.flowstudio.api.service.node.event.NodeEvent;
import com.ssafy.flowstudio.api.service.rag.VectorStoreService;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeSearchServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeSearchResponse;
import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetrieverExecutor extends NodeExecutor {

    private final VectorStoreService vectorStoreService;

    public RetrieverExecutor(RedisService redisService, ApplicationEventPublisher eventPublisher, VectorStoreService vectorStoreService, SseEmitters sseEmitters) {
        super(redisService, eventPublisher, sseEmitters);
        this.vectorStoreService = vectorStoreService;
    }

    @Override
    public void execute(Node node, Chat chat) {
        Retriever retrieverNode = (Retriever) node;

        if (retrieverNode.getKnowledge() == null) {
            throw new BaseException(ErrorCode.REQUIRED_NODE_VALUE_NOT_EXIST);
        }

        String inputMessageValue = String.valueOf(redisService.get(chat.getId(), ChatEnvVariable.INPUT_MESSAGE));

        // vector 유사도 검색
        List<String> chunks = vectorStoreService.searchVector(KnowledgeSearchServiceRequest.builder()
                .knowledge(KnowledgeSearchResponse.from(retrieverNode.getKnowledge()))
                .interval(retrieverNode.getIntervalTime())
                .topK(retrieverNode.getTopK())
                .scoreThreshold(retrieverNode.getScoreThreshold())
                .query(inputMessageValue)
                .build());

        // Redis에 Output을 업데이트한다.
        redisService.save(chat.getId(), retrieverNode.getId(), chunks.toString());

        sseEmitters.send(chat.getUser(), retrieverNode, chunks.toString());

        // 연결된 간선과 타겟 노드를 가져온다.
        if (!retrieverNode.getOutputEdges().isEmpty()) {
            Node targetNode = node.getOutputEdges().get(0).getTargetNode();

            // 타겟 노드와 chat 정보를 담은 Event를 생성한다.
            NodeEvent event = NodeEvent.of(this, targetNode, chat);

            // event를 발행한다.
            publishEvent(event);
        }
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RETRIEVER;
    }
}
