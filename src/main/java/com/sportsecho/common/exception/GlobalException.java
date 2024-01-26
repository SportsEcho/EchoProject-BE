package com.sportsecho.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final BaseErrorCode errorCode;
    private String msg;

    public GlobalException(BaseErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public GlobalException(BaseErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }
}
