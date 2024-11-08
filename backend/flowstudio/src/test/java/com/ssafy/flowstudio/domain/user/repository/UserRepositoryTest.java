package com.ssafy.flowstudio.domain.user.repository;

import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("username으로 유저를 찾는다.")
    @Test
    public void findByUsername() {
        User user1 = User.builder()
                .username("test1@gmail.com")
                .nickname("nickname1")
                .build();

        User user2 = User.builder()
                .username("test2@gmail.com")
                .nickname("nickname2")
                .build();

        userRepository.saveAll(List.of(user1, user2));

        User findUser = userRepository.findByUsername(user1.getUsername()).get();

        assertThat(findUser.getUsername()).isEqualTo(user1.getUsername());
    }

    @DisplayName("닉네임을 가진 유저가 존재하는지 확인한다.")
    @Test
    public void existsByUsername() {
        User user1 = User.builder()
                .username("test1@gmail.com")
                .nickname("nickname1")
                .build();

        userRepository.save(user1);

        boolean exists = userRepository.existsByNickname("nickname1");

        assertThat(exists).isTrue();

        exists = userRepository.existsByNickname("notExistingNickname");

        assertThat(exists).isFalse();
    }

}