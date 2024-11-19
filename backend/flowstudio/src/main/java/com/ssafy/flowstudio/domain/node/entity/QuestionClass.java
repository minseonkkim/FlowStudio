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

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_classifier_id", nullable = false)
    private QuestionClassifier questionClassifier;

    @Builder
    private QuestionClass(String content, QuestionClassifier questionClassifier) {
        this.content = content;
        this.questionClassifier = questionClassifier;
    }

    public static QuestionClass empty() {
        return QuestionClass.builder()
                .build();
    }

    public void update(String content) {
        this.content = content;
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
                    '}';
    }
}
