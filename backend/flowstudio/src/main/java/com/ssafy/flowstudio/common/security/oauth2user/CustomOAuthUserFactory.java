package com.ssafy.flowstudio.common.security.oauth2user;

import com.ssafy.flowstudio.common.security.oauth2user.response.GithubResponse;
import com.ssafy.flowstudio.common.security.oauth2user.response.GoogleResponse;
import com.ssafy.flowstudio.common.security.oauth2user.response.KakaoResponse;
import com.ssafy.flowstudio.common.security.oauth2user.response.OAuth2Response;
import com.ssafy.flowstudio.domain.user.entity.ProviderType;

import java.util.Map;

public class CustomOAuthUserFactory {
    public static OAuth2Response parseOAuth2Response(ProviderType providerType, Map<String, Object> attributes) {
        return switch (providerType) {
            case GOOGLE -> new GoogleResponse(attributes);
            case KAKAO -> new KakaoResponse(attributes);
            case GITHUB -> new GithubResponse(attributes);
        };
    }
}
