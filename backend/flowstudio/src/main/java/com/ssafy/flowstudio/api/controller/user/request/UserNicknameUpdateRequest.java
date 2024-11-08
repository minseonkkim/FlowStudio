package com.ssafy.flowstudio.api.controller.user.request;

import com.ssafy.flowstudio.api.service.user.request.UserNicknameUpdateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNicknameUpdateRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @Builder
    private UserNicknameUpdateRequest(String nickname) {
        this.nickname = nickname;
    }

    public UserNicknameUpdateServiceRequest toServiceRequest() {
        return UserNicknameUpdateServiceRequest.builder()
                .nickname(nickname)
                .build();
    }

}
