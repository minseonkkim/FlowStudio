package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chat.entity.Chat;

public interface NodeVisitor {
    void start(String message, Chat chat);
    void visit(Start start, Chat chat);
    void visit(LLM llm, Chat chat);
    void visit(QuestionClassifier questionClassifier, Chat chat);
    void visit(Retriever retriever, Chat chat);
    void visit(Conditional conditional, Chat chat);
    void visit(VariableAssigner variableAssigner, Chat chat);
    void visit(Answer answer, Chat chat);
}
