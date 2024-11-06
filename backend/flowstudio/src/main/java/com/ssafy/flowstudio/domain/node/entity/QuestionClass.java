package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuestionClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_class_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "edge_id")
    private Edge edge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_classifier_id", nullable = false)
    private QuestionClassifier questionClassifier;

    @Builder
    private QuestionClass(String content, Edge edge, QuestionClassifier questionClassifier) {
        this.content = content;
        this.edge = edge;
        this.questionClassifier = questionClassifier;
    }

    public static QuestionClass create(String content) {
        return QuestionClass.builder()
                .content(content)
                .build();
    }

    public void updateEdge(Edge edge) {
        this.edge = edge;
    }

    public void updateQuestionClassifier(QuestionClassifier questionClassifier) {
        this.questionClassifier = questionClassifier;
        questionClassifier.addQuestionClass(this);
    }

    @Override
    public String toString() {
            return "QuestionClass{" +
                    "id=" + id +
                    ", content='" + content + '\'' +
                    ", edgeId=" + (edge != null ? edge.getId() : null) +
                    '}';
    }
}
