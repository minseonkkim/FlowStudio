package com.ssafy.flowstudio.common.exception;

import com.google.api.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Authentication, Authorization
    UNAUTHORIZED(2000, HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN(2001, HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INVALID_TOKEN(2002, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(2003, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(2004, HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(2005, HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    ALREADY_AUTHORIZED(2006, HttpStatus.BAD_REQUEST, "이미 인증된 사용자입니다."),
    NOT_FOUND_USER(2007, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_ACCESS_TOKEN(2008, HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    ACCESS_DENIED(2010, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NICKNAME_ALREADY_EXISTS(2011, HttpStatus.BAD_REQUEST, "이미 존재하는 사용자 이름입니다."),


    CHAT_FLOW_NOT_FOUND(3000, HttpStatus.NOT_FOUND, "챗플로우를 찾을 수 없습니다."),

    INVALID_NODE_TYPE(4000, HttpStatus.BAD_REQUEST, "지원되지 않는 노드 타입입니다."),
    NODE_NOT_FOUND(4001, HttpStatus.NOT_FOUND, "노드를 찾을 수 없습니다."),

    CATEGORY_NOT_FOUND(5000, HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CHAT_NOT_FOUND(6000, HttpStatus.NOT_FOUND, "챗을 찾을 수 없습니다."),
    START_NODE_NOT_FOUND(6001, HttpStatus.NOT_FOUND, "시작 노드를 찾을 수 없습니다."),
    ;

    private final int code;
    private final HttpStatus status;
    private final String message;

}
