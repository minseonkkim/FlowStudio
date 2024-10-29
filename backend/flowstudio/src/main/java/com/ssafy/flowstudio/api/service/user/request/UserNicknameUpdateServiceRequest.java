package com.ssafy.flowstudio.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserNicknameUpdateServiceRequest {

    private final String nickname;

    @Builder
    private UserNicknameUpdateServiceRequest(String nickname) {
        this.nickname = nickname;
    }

}
