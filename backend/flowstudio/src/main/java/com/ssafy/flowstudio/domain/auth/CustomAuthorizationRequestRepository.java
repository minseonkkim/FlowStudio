package com.ssafy.flowstudio.domain.auth;

import com.ssafy.flowstudio.core.util.CookieUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Repository;

import static com.ssafy.flowstudio.domain.auth.AuthConst.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static com.ssafy.flowstudio.domain.auth.AuthConst.REDIRECT_URI_PARAM_COOKIE_NAME;


@Slf4j
@Repository
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final int cookieExpireSeconds = 60 * 60;

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {;
        if (authorizationRequest == null) {
            CookieUtils.removeCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.removeCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds, false);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds, false);
        }
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.removeCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.removeCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
