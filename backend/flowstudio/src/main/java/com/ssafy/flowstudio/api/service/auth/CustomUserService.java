package com.ssafy.flowstudio.api.service.auth;

import com.ssafy.flowstudio.core.exception.BaseException;
import com.ssafy.flowstudio.core.exception.ErrorCode;
import com.ssafy.flowstudio.domain.auth.response.CustomOAuth2User;
import com.ssafy.flowstudio.domain.user.User;
import com.ssafy.flowstudio.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        return new CustomOAuth2User(user);
    }

    @Bean
    public Function<UserDetails, User> fetchUser() {
        return userDetails -> userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }
}
