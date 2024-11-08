package com.ssafy.flowstudio.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.ssafy.flowstudio")
public class FeignConfig {
}
