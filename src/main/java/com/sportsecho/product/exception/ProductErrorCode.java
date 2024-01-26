package com.sportsecho.product.exception;

import com.sportsecho.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ProductErrorCode implements BaseErrorCode {

    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"),
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
