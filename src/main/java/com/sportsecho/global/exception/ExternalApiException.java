package com.sportsecho.global.exception;

public class ExternalApiException extends GlobalException {
    public ExternalApiException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ExternalApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
