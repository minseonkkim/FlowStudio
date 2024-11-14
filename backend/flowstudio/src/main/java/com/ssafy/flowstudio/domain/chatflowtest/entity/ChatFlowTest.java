package com.ssafy.flowstudio.domain.chatflowtest.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatFlowTest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_flow_test_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_flow_id", nullable = false)
    private ChatFlow chatFlow;

    @Column
    private Float embeddingDistanceMean;

    @Column
    private Float embeddingDistanceVariance;

    @Column
    private Float crossEncoderMean;

    @Column
    private Float crossEncoderVariance;

    @Column
    private Float rougeMetricMean;

    @Column
    private Float rougeMetricVariance;

    @Builder
    private ChatFlowTest(Long id, User user, ChatFlow chatFlow, Float embeddingDistanceMean, Float embeddingDistanceVariance, Float crossEncoderMean, Float crossEncoderVariance, Float rougeMetricMean, Float rougeMetricVariance) {
        this.id = id;
        this.user = user;
        this.chatFlow = chatFlow;
        this.embeddingDistanceMean = embeddingDistanceMean;
        this.embeddingDistanceVariance = embeddingDistanceVariance;
        this.crossEncoderMean = crossEncoderMean;
        this.crossEncoderVariance = crossEncoderVariance;
        this.rougeMetricMean = rougeMetricMean;
        this.rougeMetricVariance = rougeMetricVariance;
    }

    public static ChatFlowTest create(User user, ChatFlow chatFlow) {
        return ChatFlowTest.builder()
                .user(user)
                .chatFlow(chatFlow)
                .build();
    }

    public void updateResult(Float embeddingDistanceMean, Float embeddingDistanceVariance, Float crossEncoderMean, Float crossEncoderVariance, Float rougeMetricMean, Float rougeMetricVariance) {
        this.embeddingDistanceMean = embeddingDistanceMean;
        this.embeddingDistanceVariance = embeddingDistanceVariance;
        this.crossEncoderMean = crossEncoderMean;
        this.crossEncoderVariance = crossEncoderVariance;
        this.rougeMetricMean = rougeMetricMean;
        this.rougeMetricVariance = rougeMetricVariance;
    }
}
