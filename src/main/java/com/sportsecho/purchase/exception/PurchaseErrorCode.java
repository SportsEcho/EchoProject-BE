package com.sportsecho.purchase.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PurchaseErrorCode implements BaseErrorCode {

    EMPTY_CART(HttpStatus.BAD_REQUEST, "장바구니가 비어있습니다."),
    EMPTY_PURCHASE_LIST(HttpStatus.BAD_REQUEST, "구매 내역이 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
