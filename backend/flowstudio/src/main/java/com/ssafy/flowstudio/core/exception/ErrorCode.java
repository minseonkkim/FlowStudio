package com.ssafy.flowstudio.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ;

    private final int code;
    private final HttpStatus status;
    private final String message;

}
