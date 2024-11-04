package com.ssafy.flowstudio.domain.node.entity;

public interface NodeVisitor {
    void visit(Start start);
    void visit(LLM llm);
    void visit(QuestionClassifier questionClassifier);
    void visit(Retriever retriever);
    void visit(Conditional conditional);
    void visit(VariableAssigner variableAssigner);
    void visit(Answer answer);
}
