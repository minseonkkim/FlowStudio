package com.ssafy.flowstudio.api.controller.user;


import com.ssafy.flowstudio.api.controller.user.request.UserNicknameUpdateRequest;
import com.ssafy.flowstudio.api.service.user.UserService;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 유저 정보 조회
     * @param user
     * @return UserResponse
     */
    @GetMapping(value = "/api/v1/users/me")
    public ApiResponse<UserResponse> getUser(
            @CurrentUser User user
    ) {
        return ApiResponse.ok(userService.getUser(user));
    }

    /**
     * 닉네임 중복 체크
     * @param nickname
     * @return null
     */
    @GetMapping(value = "/api/v1/users/check-nickname")
    public ApiResponse<Void> checkNickname(
            @CurrentUser User user,
            @RequestParam @NotBlank String nickname
    ) {
        System.out.println("user = " + user);

        userService.checkNickname(nickname);
        return ApiResponse.ok();
    }

    /**
     * 닉네임 변경
     * @param user
     * @param request
     * @return UserResponse
     */
    @PatchMapping(value = "/api/v1/users/nickname")
    public ApiResponse<UserResponse> updateNickname(
            @CurrentUser User user,
            @Valid @RequestBody UserNicknameUpdateRequest request
    ) {
        userService.updateNickname(user, request.toServiceRequest());
        return ApiResponse.ok(userService.getUser(user));
    }


    @PatchMapping(value = "/api/v1/users/profile-image")
    public ApiResponse<UserResponse> updateProfileImage(
            @CurrentUser User user,
            @RequestPart(value = "image") MultipartFile image
    ) {
        userService.updateProfileImage(user, image);
        return ApiResponse.ok(userService.getUser(user));
    }

}
