package com.ssafy.flowstudio.domain.user;

import com.ssafy.flowstudio.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = "username")}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImage;

    @Column(name = "provider_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Builder
    private User(Long id, String username, String nickname, String profileImage, ProviderType providerType) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.providerType = providerType;
    }

    public static User create(String username, String nickname, String profileImage, ProviderType providerType) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .profileImage(profileImage)
                .providerType(providerType)
                .build();
    }

}
