package com.sportsecho.global.exception;

public class ExternalApiException extends GlobalException {
    public ExternalApiException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ExternalApiException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
