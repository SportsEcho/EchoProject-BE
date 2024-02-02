package com.sportsecho.hotdeal.exception;

import com.sportsecho.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum HotdealErrorCode implements BaseErrorCode {
    NOT_FOUND_HOTDEAL(HttpStatus.NOT_FOUND, "핫딜을 찾을 수 없습니다"),
    LACK_DEAL_QUANTITY(HttpStatus.BAD_REQUEST, "구매 하고자 하는 갯수가 남은 한정수량 보다 많습니다"),
    QUEUE_NOT_POSSIBLE_DUE_TO_HIGH_DEMAND(HttpStatus.BAD_REQUEST, "현재 구매 요청이 많아 대기열에 추가할 수 없습니다"),
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    SOLD_OUT(HttpStatus.NOT_FOUND, "품절된 상품입니다."),
    NOT_IN_WAIT_QUEUE(HttpStatus.NOT_ACCEPTABLE, "구매 순서가 아닙니다 어떻게 들어오셨죠?.");

    private final HttpStatus status;
    private final String msg;
}
