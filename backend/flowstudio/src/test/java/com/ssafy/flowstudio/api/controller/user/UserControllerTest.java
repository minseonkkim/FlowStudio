package com.ssafy.flowstudio.api.controller.user;

import com.ssafy.flowstudio.api.controller.user.request.UserNicknameUpdateRequest;
import com.ssafy.flowstudio.api.service.user.response.TokenUsageLogResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("유저가 자신의 닉네임을 조회한다.")
    @WithMockUser
    @Test
    public void getUser() throws Exception {
        // given
        User user = User.builder()
                .username("username")
                .nickname("nickname")
                .build();

        given(userService.getUser(any(User.class)))
                .willReturn(UserResponse.from(user));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("유저가 닉네임 중복을 확인했을 때 중복된 닉네임이 없다.")
    @WithMockUser
    @Test
    public void checkNickname() throws Exception {
        // given
        String nickname = "nickname";

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/users/check-nickname")
                        .with(csrf())
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @DisplayName("유저가 올바른 닉네임으로 닉네임을 수정하면 성공한다.")
    @WithMockUser
    @Test
    public void changeUserNickname() throws Exception {
        // given
        UserNicknameUpdateRequest request = UserNicknameUpdateRequest.builder()
                .nickname("New nickname")
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .nickname("New nickname")
                .build();

        given(userService.getUser(any(User.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/api/v1/users/nickname")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nickname").value("New nickname"));
    }

    @DisplayName("유저가 빈 닉네임으로 닉네임을 수정하면 실패한다.")
    @WithMockUser
    @Test
    public void changeUserNicknameEmpty() throws Exception {
        // given
        UserNicknameUpdateRequest request = UserNicknameUpdateRequest.builder()
                .nickname("")
                .build();

        // when
        ResultActions perform = mockMvc.perform(
                patch("/api/v1/users/nickname")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("닉네임은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("토큰 사용로그를 조회한다.")
    @WithMockUser
    @Test
    public void getTokenUsageLog() throws Exception {
        // given
        TokenUsageLogResponse response = TokenUsageLogResponse.builder()
                .id(1L)
                .tokenUsage(1)
                .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();

        given(userService.getTokenUsageLogs(any(User.class)))
                .willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/users/token-usage-logs")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

}
