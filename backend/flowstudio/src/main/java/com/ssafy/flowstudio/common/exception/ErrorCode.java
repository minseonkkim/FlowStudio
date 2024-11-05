package com.ssafy.flowstudio.common.exception;

import com.google.api.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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

    KNOWLEDGE_NOT_FOUND(7000, HttpStatus.NOT_FOUND, "지식베이스를 찾을 수 없습니다."),
    KNOWLEDGE_INSERT_UNAVAILABLE(7001, HttpStatus.SERVICE_UNAVAILABLE, "지식베이스를 등록할 수 없습니다."),

    EMPTY_FILE_EXCEPTION(8001, HttpStatus.BAD_REQUEST, "파일이 유효하지 않습니다."),
    NO_FILE_EXTENSION(8002, HttpStatus.BAD_REQUEST, "파일 확장자 명이 없습니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(8003, INTERNAL_SERVER_ERROR, "이미지 업로드 중 입출력 오류가 발생했습니다."),
    INVALID_FILE_EXTENSION(8004, HttpStatus.BAD_REQUEST, "유효하지 않은 확장자 명입니다."),
    PUT_OBJECT_EXCEPTION(8005, INTERNAL_SERVER_ERROR, "S3에 파일 업로드 중 오류가 발생했습니다."),
    FILE_SIZE_EXCEEDS_LIMIT(8006, HttpStatus.BAD_REQUEST, "파일 크기가 제한을 초과했습니다. (최대 1MB)"),
    ;

    private final int code;
    private final HttpStatus status;
    private final String message;

}
