package com.ssafy.flowstudio.core.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
    private Errors errors;

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public BaseException(ErrorCode errorCode, Errors errors) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
