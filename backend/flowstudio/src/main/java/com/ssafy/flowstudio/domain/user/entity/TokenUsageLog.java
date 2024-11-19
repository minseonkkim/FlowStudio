package com.ssafy.flowstudio.domain.user.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenUsageLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_usage_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private int tokenUsage;

    @Builder
    private TokenUsageLog(Long id, User user, int tokenUsage) {
        this.id = id;
        this.user = user;
        this.tokenUsage = tokenUsage;
    }

    public static TokenUsageLog create(User user, int tokenUsage) {
        return TokenUsageLog.builder()
                .user(user)
                .tokenUsage(tokenUsage)
                .build();
    }

}
