package com.sportsecho.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "사용자 이름이 중복되었습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 유효하지 않습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다"),
    INVALID_ID_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 유효하지 않습니다"),
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "외부 API 오류입니다"),
    INVALID_API_RESPONSE(HttpStatus.BAD_REQUEST, "유효하지 않은 API 응답입니다");

    private HttpStatus status;
    private String msg;
}
