package com.ssafy.flowstudio.common.secret;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.api-keys")
public class SecretKeyProperties {
    private String openAi;
    private String claude;
    private String gemini;
    private String clova;
}