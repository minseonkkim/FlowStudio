package com.ssafy.flowstudio.api.controller.auth;

import com.ssafy.flowstudio.api.ApiResponse;
import com.ssafy.flowstudio.api.service.auth.JWTService;
import com.ssafy.flowstudio.core.util.CookieUtils;
import com.ssafy.flowstudio.domain.auth.AuthConst;
import com.ssafy.flowstudio.domain.auth.jwt.JwtProperties;
import com.ssafy.flowstudio.domain.auth.jwt.JwtToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final JWTService jwtService;
    private final JwtProperties properties;

    @PostMapping("/reissue")
    public ApiResponse<String> reissue(
            HttpServletRequest request, HttpServletResponse response
    ) {
        Optional<Cookie> refresh = CookieUtils.getCookie(request, "refresh");

        if (refresh.isEmpty()) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, "Refresh token not found", null);
        }

        JwtToken token = jwtService.refreshToken(refresh.get().getValue());

        CookieUtils.addCookie(response, AuthConst.REFRESH_TOKEN, token.getRefreshToken(),properties.getRefreshExpire(), true);
        response.setHeader("accessToken", token.getAccessToken());

        return ApiResponse.ok("refresh token success");
    }

}
