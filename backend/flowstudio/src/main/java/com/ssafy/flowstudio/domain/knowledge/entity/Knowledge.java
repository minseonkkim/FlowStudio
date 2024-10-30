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
    @Column(name = "document_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String title;

    @Column
    private boolean isPublic;

    @Builder
    private Knowledge(Long id, User user, String title, boolean isPublic) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.isPublic = isPublic;
    }

    public static Knowledge create(User user, String title, boolean isPublic) {
        return Knowledge.builder()
                .user(user)
                .title(title)
                .isPublic(isPublic)
                .build();
    }
}
