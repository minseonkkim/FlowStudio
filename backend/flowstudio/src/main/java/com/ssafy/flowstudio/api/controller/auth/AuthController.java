package com.ssafy.flowstudio.api.controller.auth;

import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.common.security.jwt.JWTService;
import com.ssafy.flowstudio.common.util.CookieUtils;
import com.ssafy.flowstudio.common.constant.AuthConst;
import com.ssafy.flowstudio.common.security.jwt.JwtProperties;
import com.ssafy.flowstudio.common.security.jwt.JwtToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final JWTService jwtService;
    private final JwtProperties properties;

    @PostMapping("/api/v1/auth/reissue")
    public ApiResponse<String> reissue(
            HttpServletRequest request, HttpServletResponse response
    ) {
        Optional<Cookie> refresh = CookieUtils.getCookie(request, AuthConst.REFRESH_TOKEN);

        if (refresh.isEmpty()) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, "Refresh token not found", null);
        }

        JwtToken token = jwtService.refreshToken(refresh.get().getValue());

        CookieUtils.addCookie(response, AuthConst.REFRESH_TOKEN, token.getRefreshToken(),properties.getRefreshExpire(), true);
        response.setHeader("Authorization", token.getAccessToken());

        return ApiResponse.ok("refresh token success");
    }

}
