package com.ssafy.flowstudio.domain.user.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "api_key_id")
    private ApiKey apiKey;

    @Column(nullable = false)
    private String username;

    @Column
    private String nickname;

    @Column
    private String profileImage;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Builder
    private User(Long id, ApiKey apiKey, String username, String nickname, String profileImage, ProviderType providerType) {
        this.id = id;
        this.apiKey = apiKey;
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.providerType = providerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static User create(String username, String nickname, String profileImage, ProviderType providerType) {
        return User.builder()
                .username(username)
                .apiKey(ApiKey.empty())
                .nickname(nickname)
                .profileImage(profileImage)
                .providerType(providerType)
                .build();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String imageUrl) {
        this.profileImage = imageUrl;
    }
}
