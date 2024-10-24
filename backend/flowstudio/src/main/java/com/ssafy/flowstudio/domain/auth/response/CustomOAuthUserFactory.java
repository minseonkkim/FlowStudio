package com.ssafy.flowstudio.domain.auth.response;

import com.ssafy.flowstudio.domain.auth.response.impl.GithubResponse;
import com.ssafy.flowstudio.domain.auth.response.impl.GoogleResponse;
import com.ssafy.flowstudio.domain.auth.response.impl.KakaoResponse;
import com.ssafy.flowstudio.domain.user.ProviderType;

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
