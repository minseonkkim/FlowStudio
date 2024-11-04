package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.executor.NodeExecutor;
import com.ssafy.flowstudio.domain.node.entity.*;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NodeVisitorImpl implements NodeVisitor {

    private final Map<NodeType, NodeExecutor> executors = new EnumMap<>(NodeType.class);

    public NodeVisitorImpl(List<NodeExecutor> executorList) {
        for (NodeExecutor executor : executorList) {
            executors.put(executor.getNodeType(), executor);
        }
    }

    @Override
    public void visit(Start start) {
        executors.get(start.getType()).execute(start);
    }

    @Override
    public void visit(LLM llm) {
        executors.get(llm.getType()).execute(llm);
    }

    @Override
    public void visit(QuestionClassifier questionClassifier) {
        executors.get(questionClassifier.getType()).execute(questionClassifier);
    }

    @Override
    public void visit(Retriever retriever) {
        executors.get(retriever.getType()).execute(retriever);
    }

    @Override
    public void visit(Conditional conditional) {
        executors.get(conditional.getType()).execute(conditional);
    }

    @Override
    public void visit(VariableAssigner variableAssigner) {
        executors.get(variableAssigner.getType()).execute(variableAssigner);
    }

    @Override
    public void visit(Answer answer) {
        executors.get(answer.getType()).execute(answer);
    }

}
