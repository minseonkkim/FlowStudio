package com.ssafy.flowstudio.domain.chatflowtest.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.persistence.*;
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
    private int totalTestCount;

    @Column
    private int successCount;

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

    @OneToMany(mappedBy = "chatFlowTest", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatFlowTestCase> chatFlowTestCases = new ArrayList<>();

    @Builder
    private ChatFlowTest(Long id, User user, ChatFlow chatFlow, int totalTestCount, int successCount, Float embeddingDistanceMean, Float embeddingDistanceVariance, Float crossEncoderMean, Float crossEncoderVariance, Float rougeMetricMean, Float rougeMetricVariance) {
        this.id = id;
        this.user = user;
        this.chatFlow = chatFlow;
        this.totalTestCount = totalTestCount;
        this.successCount = successCount;
        this.embeddingDistanceMean = embeddingDistanceMean;
        this.embeddingDistanceVariance = embeddingDistanceVariance;
        this.crossEncoderMean = crossEncoderMean;
        this.crossEncoderVariance = crossEncoderVariance;
        this.rougeMetricMean = rougeMetricMean;
        this.rougeMetricVariance = rougeMetricVariance;
    }

    public static ChatFlowTest create(User user, ChatFlow chatFlow, int totalTestCount) {
        return ChatFlowTest.builder()
                .user(user)
                .chatFlow(chatFlow)
                .totalTestCount(totalTestCount)
                .successCount(0)
                .build();
    }

    public void updateResult(List<Float> result) {
        this.embeddingDistanceMean = result.get(0);
        this.embeddingDistanceVariance = result.get(1);
        this.crossEncoderMean = result.get(2);
        this.crossEncoderVariance = result.get(3);
        this.rougeMetricMean = result.get(4);
        this.rougeMetricVariance = result.get(5);
    }

    public void incrementSuccessCount() {
        this.successCount += 1;
    }

    public boolean isCompleted() {
        return this.totalTestCount == this.successCount;
    }

    public void addChatFlowTestCase(ChatFlowTestCase chatFlowTestCase) {
        this.chatFlowTestCases.add(chatFlowTestCase);
    }

}
