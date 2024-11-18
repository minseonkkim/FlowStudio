package com.ssafy.flowstudio.domain.chatflowtest.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatFlowTestCase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_flow_test_case_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_flow_test_id", nullable = false)
    private ChatFlowTest chatFlowTest;

    @Lob
    private String question;

    @Lob
    private String groundTruth;

    @Lob
    private String prediction;

    @Column
    private Float embeddingDistance;

    @Column
    private Float crossEncoder;

    @Column
    private Float rougeMetric;

    @Builder
    private ChatFlowTestCase(ChatFlowTest chatFlowTest, String question, String groundTruth, String prediction, Float embeddingDistance, Float crossEncoder, Float rougeMetric) {
        this.chatFlowTest = chatFlowTest;
        this.question = question;
        this.groundTruth = groundTruth;
        this.prediction = prediction;
        this.embeddingDistance = embeddingDistance;
        this.crossEncoder = crossEncoder;
        this.rougeMetric = rougeMetric;
    }

    public static ChatFlowTestCase create(ChatFlowTest chatFlowTest, String question, String groundTruth, String prediction, Float embeddingDistance, Float crossEncoder, Float rougeMetric) {
        return ChatFlowTestCase.builder()
                .chatFlowTest(chatFlowTest)
                .question(question)
                .groundTruth(groundTruth)
                .prediction(prediction)
                .embeddingDistance(embeddingDistance)
                .crossEncoder(crossEncoder)
                .rougeMetric(rougeMetric)
                .build();
    }

}
