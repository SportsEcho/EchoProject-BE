package com.sportsecho.game.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GameErrorCode implements BaseErrorCode {
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "외부 API 오류입니다"),
    INVALID_API_RESPONSE(HttpStatus.BAD_REQUEST, "유효하지 않은 API 응답입니다");

    private final HttpStatus status;
    private final String msg;
}
