package com.ssafy.flowstudio.common.security.jwt;

import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JWTService {
    private final JwtProperties properties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(properties.getSecret().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public JwtToken generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        String authority = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        Date accessTokenExpire = new Date(now + properties.getAccessExpire());
        Date refreshTokenExpire = new Date(now + properties.getRefreshExpire());

        String accessToken = Jwts.builder()
                .header().add("typ", "JWT").add("alg", "HS256")
                .and()
                .subject(username)
                .claim("authorities", authority)
                .issuedAt(new Date(now))
                .expiration(accessTokenExpire)
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(username)
                .claim("authorities", authority)
                .issuedAt(new Date(now))
                .expiration(refreshTokenExpire)
                .signWith(secretKey)
                .compact();

        return JwtToken.of(accessToken, refreshToken);
    }


    private Claims parseClaims(String accessToken) {
        String message;
        Exception exception;
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            message = "유효기간이 만료된 토큰입니다.";
            exception = e;
        } catch (MalformedJwtException e) {
            message = "잘못된 형식의 토큰입니다.";
            exception = e;
        } catch (IllegalArgumentException e) {
            message = "잘못된 인자입니다.";
            exception = e;
        } catch (SignatureException e) {
            message = "서명이 일치하지 않습니다.";
            exception = e;
        } catch (Exception e) {
            message = "토큰 파싱 중 에러가 발생했습니다.";
            exception = e;
        }

        throw new BaseException(ErrorCode.INVALID_ACCESS_TOKEN, message, exception);
    }

    public JwtToken refreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return generateToken(claims.getSubject(), authorities);
    }

    public Authentication parseAuthentication(String accessToken) throws Exception {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map((authority) -> new SimpleGrantedAuthority("ROLE_" + authority))
                        .toList();

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

}
