package com.ssafy.flowstudio.api.service.user.response;

import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String username;
    private final String nickname;
    private final String profileImage;

    @Builder
    private UserResponse(Long id, String username, String nickname, String profileImage) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

}
