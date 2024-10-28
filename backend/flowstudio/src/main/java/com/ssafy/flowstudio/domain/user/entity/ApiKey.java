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
    private String open_ai_key;

    @Column
    private String cloude_key;

    @Column
    private String gemini_key;

    @Column
    private String clova_key;

    @Builder
    private ApiKey(Long id, String open_ai_key, String cloude_key, String gemini_key, String clova_key) {
        this.id = id;
        this.open_ai_key = open_ai_key;
        this.cloude_key = cloude_key;
        this.gemini_key = gemini_key;
        this.clova_key = clova_key;
    }

    public static ApiKey empty() {
        return ApiKey.builder()
                .build();
    }

}
