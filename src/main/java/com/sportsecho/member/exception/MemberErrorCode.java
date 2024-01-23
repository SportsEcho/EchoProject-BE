package com.sportsecho.member.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),

    INVALID_AUTH(HttpStatus.UNAUTHORIZED, "유효하지 않은 로그인 정보입니다"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    INVALID_ADMIN_KEY(HttpStatus.UNAUTHORIZED, "유효하지 않은 관리자 키입니다."),

    INVALID_MEMBER_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 이름입니다."),

    ;

    private final HttpStatus status;
    private final String msg;
}
