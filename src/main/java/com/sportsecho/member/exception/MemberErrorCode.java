package com.sportsecho.member.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    INVALID_AUTH(HttpStatus.UNAUTHORIZED, "유효하지 않은 로그인 정보입니다");

    private final HttpStatus status;
    private final String msg;
}
