package com.ssafy.flowstudio.support;

import com.ssafy.flowstudio.domain.user.entity.ProviderType;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user = User.create("test-user", "test-user", ProviderType.GOOGLE);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    }
}
