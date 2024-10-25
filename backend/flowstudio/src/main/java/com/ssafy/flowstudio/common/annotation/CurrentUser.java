package com.ssafy.flowstudio.common.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : @fetchUser.apply(#this)", errorOnInvalidType = true)
public @interface CurrentUser {
}
