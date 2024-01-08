package com.sportsecho.product.exception;

import com.sportsecho.global.exception.GlobalException;

public class ProductException extends GlobalException {
    public ProductException(ProductErrorCode errorCode) {
        super(errorCode);
    }
}