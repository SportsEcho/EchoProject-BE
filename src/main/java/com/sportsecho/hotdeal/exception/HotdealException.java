package com.sportsecho.hotdeal.exception;

import com.sportsecho.global.exception.GlobalException;

public class  HotdealException extends GlobalException {
    public HotdealException(HotdealErrorCode errorCode) {
        super(errorCode);
    }
}