package com.sportsecho.hotdeal.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum HotdealErrorCode implements BaseErrorCode {
    NOT_FOUND_HOTDEAL(HttpStatus.NOT_FOUND, "핫딜을 찾을 수 없습니다"),
    LACK_DEAL_QUANTITY(HttpStatus.BAD_REQUEST, "구매 하고자 하는 갯수가 남은 한정수량 보다 많습니다"),
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
