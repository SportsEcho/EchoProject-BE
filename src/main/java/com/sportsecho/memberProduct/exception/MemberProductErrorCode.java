package com.sportsecho.memberProduct.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberProductErrorCode implements BaseErrorCode {

    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),
    NOT_FOUND_PRODUCT_IN_CART(HttpStatus.NOT_FOUND, "장바구니에 상품이 존재하지 않습니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "수정/삭제 권한이 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
