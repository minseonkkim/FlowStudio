package com.ssafy.flowstudio.support;

import com.ssafy.flowstudio.domain.user.entity.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected Function<UserDetails, User> fetchUser;

}
