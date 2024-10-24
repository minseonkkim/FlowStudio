package com.ssafy.flowstudio.domain.auth.handler;


import com.ssafy.flowstudio.api.service.auth.JWTService;
import com.ssafy.flowstudio.core.util.CookieUtils;
import com.ssafy.flowstudio.domain.auth.AuthConst;
import com.ssafy.flowstudio.domain.auth.CustomAuthorizationRequestRepository;
import com.ssafy.flowstudio.domain.auth.jwt.JwtProperties;
import com.ssafy.flowstudio.domain.auth.jwt.JwtToken;
import com.ssafy.flowstudio.domain.auth.response.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static com.ssafy.flowstudio.domain.auth.AuthConst.REDIRECT_URI_PARAM_COOKIE_NAME;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final JwtProperties properties;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        clearAuthenticationAttributes(request, response);
        return redirectUri.orElse(getDefaultTargetUrl());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        JwtToken jwtToken = jwtService.generateToken(customUserDetails.getUsername(), customUserDetails.getAuthorities());
        CookieUtils.addCookie(response, AuthConst.REFRESH_TOKEN, jwtToken.getRefreshToken(), properties.getRefreshExpire(), true);


        String redirectURI = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(redirectURI, jwtToken));

    }

    private String getRedirectUrl(String targetUrl, JwtToken token) {
        return UriComponentsBuilder.fromUriString(targetUrl + "/auth/callback")
                .queryParam("accessToken", token.getAccessToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        customAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
