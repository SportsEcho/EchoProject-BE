package com.sportsecho.common.oauth.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OAuthErrorCode implements BaseErrorCode {

    ILLEGAL_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 OAuth 로그인 요청입니다."),
    ;

    private final HttpStatus status;
    private final String msg;
}
