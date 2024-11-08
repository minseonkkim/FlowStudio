package com.ssafy.flowstudio.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_key_id")
    private Long id;

    @Column
    private String openAiKey;

    @Column
    private String claudeKey;

    @Column
    private String geminiKey;

    @Column
    private String clovaKey;

    @Builder
    private ApiKey(Long id, String openAiKey, String claudeKey, String geminiKey, String clovaKey) {
        this.id = id;
        this.openAiKey = openAiKey;
        this.claudeKey = claudeKey;
        this.geminiKey = geminiKey;
        this.clovaKey = clovaKey;
    }

    public void update(String openAiKey, String claudeKey, String geminiKey, String clovaKey) {
        this.openAiKey = openAiKey;
        this.claudeKey = claudeKey;
        this.geminiKey = geminiKey;
        this.clovaKey = clovaKey;
    }

    public static ApiKey empty() {
        return ApiKey.builder()
                .build();
    }

}
