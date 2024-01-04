package com.sportsecho.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GlobalException extends RuntimeException{
    private ErrorCode errorCode;
    private String msg;

    public GlobalException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.msg = null;
    }
}

