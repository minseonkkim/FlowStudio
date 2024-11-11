package com.ssafy.flowstudio.common.config;


import com.ssafy.flowstudio.api.service.auth.CustomOAuth2UserService;
import com.ssafy.flowstudio.common.security.filter.JWTFilter;
import com.ssafy.flowstudio.common.security.repository.CustomAuthorizationRequestRepository;
import com.ssafy.flowstudio.common.security.handler.CustomAuthenticationDeniedHandler;
import com.ssafy.flowstudio.common.security.handler.CustomAuthenticationEntryPoint;
import com.ssafy.flowstudio.common.security.handler.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthenticationDeniedHandler authenticationDeniedHandler;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final JWTFilter jwtFilter;

    @Value("${spring.aes.symmetric.key}")
    private String symmetrickey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:13000", "https://k11c201.p.ssafy.io"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    configuration.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
                    return configuration;
                }))
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(authenticationDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/",
                                "/api/v1/auth/**",
                                "/actuator/**",
                                "/login/**",
                                "/oauth2/**",
                                "/api/v1/chat-flows/{chatFlowId}/chats",
                                "/api/v1/sse/connect"
                        ).permitAll()
                        .anyRequest().authenticated())
                .oauth2Login((oauth2) -> oauth2
                        .authorizationEndpoint(authorization ->
                                authorization.baseUri("/oauth2/authorization")
                                        .authorizationRequestRepository(customAuthorizationRequestRepository)
                        )
                        .redirectionEndpoint(redirection -> redirection.baseUri("/*/oauth2/code/*"))
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(symmetrickey, "2345436820457230");
    }
}
