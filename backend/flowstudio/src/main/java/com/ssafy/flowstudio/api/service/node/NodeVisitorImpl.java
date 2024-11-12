package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.executor.NodeExecutor;
import com.ssafy.flowstudio.common.constant.ChatEnvVariable;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.node.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NodeVisitorImpl implements NodeVisitor {

    private final Map<NodeType, NodeExecutor> executors = new EnumMap<>(NodeType.class);
    private final RedisService redisService;

    // NodeExecutor를 상속받은 Inheritor Bean들을 찾아서 리스트로 주입
    public NodeVisitorImpl(List<NodeExecutor> executorList, RedisService redisService) {
        for (NodeExecutor executor : executorList) {
            executors.put(executor.getNodeType(), executor);
        }
        this.redisService = redisService;
    }

    @Override
    public void start(String message, Chat chat) {
        List<Node> nodes = chat.getChatFlow().getNodes();

        Node startNode = nodes.stream()
                .filter(node -> node.getType().equals(NodeType.START))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode.START_NODE_NOT_FOUND));

        redisService.save(chat.getId(), ChatEnvVariable.INPUT_MESSAGE ,message);

        startNode.accept(this, chat);
    }

    @Override
    public void visit(Start start, Chat chat) {
        executors.get(start.getType()).execute(start, chat);
    }

    @Override
    public void visit(LLM llm, Chat chat) {
        executors.get(llm.getType()).execute(llm, chat);
    }

    @Override
    public void visit(QuestionClassifier questionClassifier, Chat chat) {
        executors.get(questionClassifier.getType()).execute(questionClassifier, chat);
    }

    @Override
    public void visit(Retriever retriever, Chat chat) {
        executors.get(retriever.getType()).execute(retriever, chat);
    }

    @Override
    public void visit(Answer answer, Chat chat) {
        executors.get(answer.getType()).execute(answer, chat);
    }

    @Deprecated
    @Override
    public void visit(Conditional conditional, Chat chat) {
        executors.get(conditional.getType()).execute(conditional, chat);
    }

    @Deprecated
    @Override
    public void visit(VariableAssigner variableAssigner, Chat chat) {
        executors.get(variableAssigner.getType()).execute(variableAssigner, chat);
    }

}
