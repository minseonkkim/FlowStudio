package com.ssafy.flowstudio.api.service.auth;

import com.ssafy.flowstudio.domain.auth.response.CustomOAuth2User;
import com.ssafy.flowstudio.domain.auth.response.CustomOAuthUserFactory;
import com.ssafy.flowstudio.domain.auth.response.OAuth2Response;
import com.ssafy.flowstudio.domain.user.ProviderType;
import com.ssafy.flowstudio.domain.user.User;
import com.ssafy.flowstudio.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());
        OAuth2Response oauth2Response = CustomOAuthUserFactory.parseOAuth2Response(providerType,
                oauth2User.getAttributes());

        String username = oauth2Response.getEmail();
        String profileImage = oauth2Response.getProfileImage();
        String nickname = oauth2Response.getNickname();

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(User.create(username, nickname, profileImage, providerType)));

        return new CustomOAuth2User(user);
    }

}
