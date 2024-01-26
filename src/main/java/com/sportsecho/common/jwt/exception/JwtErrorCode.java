package com.sportsecho.common.jwt.exception;

import com.sportsecho.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements BaseErrorCode {

    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다."),
    SIGNATURE_EXCEPTION(HttpStatus.UNAUTHORIZED, "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.UNAUTHORIZED, "JWT claims is empty, 잘못된 JWT 토큰 입니다."),
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "Expired JWT token, 만료된 JWT token 입니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "갱신토큰이 존재하지 않습니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "접근토큰이 존재하지 않습니다."),

    ;

    private final HttpStatus status;
    private final String msg;
}