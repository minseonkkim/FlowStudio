package com.ssafy.flowstudio.domain.knowledge.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.user.entity.User;
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
public class Knowledge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "knowledge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String title;

    @Column
    private boolean isPublic;

    @Column
    private Integer totalToken;

    @Builder
    private Knowledge(Long id, User user, String title, boolean isPublic, Integer totalToken) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.isPublic = isPublic;
        this.totalToken = totalToken;
    }

    public static Knowledge create(User user, String title, boolean isPublic, int totalToken) {
        return Knowledge.builder()
                .user(user)
                .title(title)
                .isPublic(isPublic)
                .totalToken(totalToken)
                .build();
    }

    public void update(String title, boolean isPublic) {
        this.title = title;
        this.isPublic = isPublic;
    }

    public void updateToken(Integer totalToken) {
        this.totalToken = totalToken;
    }
}
