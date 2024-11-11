package com.ssafy.flowstudio.api.service.user;

import com.ssafy.flowstudio.api.service.user.request.UserNicknameUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.user.response.TokenUsageLogResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLog;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLogRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Transactional
class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenUsageLogRepository tokenUsageLogRepository;

    @DisplayName("로그인한 유저의 정보를 조회한다")
    @Test
    public void getUser() {
        // given
        User user = User.builder()
                .username("test@gmail.com")
                .nickname("nickname")
                .build();
        userRepository.save(user);

        // when
        UserResponse response = userService.getUser(user);

        // then
        assertThat(response)
                .extracting("username", "nickname")
                .contains("test@gmail.com", "nickname");
    }

    @DisplayName("없는 유저를 조회한다")
    @Test
    public void getNotExistUser() {
        User user = User.builder()
                .username("test@gmail.com")
                .nickname("nickname")
                .build();

        assertThatThrownBy(() -> userService.getUser(user))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorCode.NOT_FOUND_USER.getMessage());
    }

    @DisplayName("중복된 닉네임을 확인했을 때 중복된 닉네임이 없으면 성공 메시지를 반환한다.")
    @Test
    public void checkNicknameSuccess() {
        // given
        boolean result = userService.checkNickname("newNickname");

        // when, then
        assertTrue(result);
    }

    @DisplayName("중복된 닉네임을 확인했을 때 중복된 닉네임이 있으면 예외가 발생한다.")
    @Test
    public void checkNicknameFail() {
        // given
        User user = User.builder()
                .username("example@google.com")
                .nickname("existNickname")
                .build();

        userRepository.save(user);

        // when, then
        assertThatThrownBy(() -> userService.checkNickname("existNickname"))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorCode.NICKNAME_ALREADY_EXISTS.getMessage());
    }

    @DisplayName("유저가 닉네임을 수정하면 수정된 닉네임을 반환한다.")
    @Test
    public void updateNickname() {
        // given
        User user = User.builder()
                .username("test@gmail.com")
                .nickname("nickname")
                .build();

        UserNicknameUpdateServiceRequest request = UserNicknameUpdateServiceRequest.builder()
                .nickname("newNickname")
                .build();

        userRepository.save(user);

        // when
        userService.updateNickname(user, request);

        // then
        assertThat(user.getNickname()).isEqualTo("newNickname");
    }

    @DisplayName("이미 존재하는 닉네임으로 수정하려고 하면 예외가 발생한다.")
    @Test
    public void updateUsernameWithExistingUsername() {
        User user1 = User.builder()
                .username("test1@gmail.com")
                .nickname("nickname1")
                .build();

        User user2 = User.builder()
                .username("test2@gmail.com")
                .nickname("nickname2")
                .build();

        userRepository.saveAll(List.of(user1, user2));

        UserNicknameUpdateServiceRequest request = UserNicknameUpdateServiceRequest.builder()
                .nickname("nickname1")
                .build();

        assertThatThrownBy(() -> userService.updateNickname(user2, request))
                .isInstanceOf(BaseException.class)
                .hasMessage(ErrorCode.NICKNAME_ALREADY_EXISTS.getMessage());
    }

    @DisplayName("토큰 사용기록을 90일 전까지 조회한다.")
    @Test
    public void getTokenUsageLogs() {
        // given
        ApiKey apiKey = ApiKey.empty();

        User user = User.builder()
                .username("username")
                .apiKey(apiKey)
                .build();

        TokenUsageLog log1 = TokenUsageLog.builder()
                .user(user)
                .tokenUsage(10)
                .build();

        TokenUsageLog log2 = TokenUsageLog.builder()
                .user(user)
                .tokenUsage(20)
                .build();

        userRepository.save(user);
        tokenUsageLogRepository.saveAll(List.of(log1, log2));

        // when
        List<TokenUsageLogResponse> response = userService.getTokenUsageLogs(user);

        // then
        assertThat(response)
                .hasSize(2)
                .extracting("tokenUsage")
                .contains(10, 20);
    }

}
