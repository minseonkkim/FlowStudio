package com.ssafy.flowstudio.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNicknameUpdateServiceRequest {

    private String nickname;

    @Builder
    private UserNicknameUpdateServiceRequest(String nickname) {
        this.nickname = nickname;
    }

}
