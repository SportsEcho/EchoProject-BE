package com.sportsecho.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiResponse<T> {

    private final String msg;
    private final Integer code;
    private final T data;

    public static <T> ApiResponse<T> of(String msg, Integer code, T data) {
        return new ApiResponse<>(msg, code, data);
    }

    public static <T> ApiResponse<T> of(ResponseCode responseCode, T data) {
        return new ApiResponse<>(
            responseCode.getMessage(),
            responseCode.getHttpstatus(),
            data
        );
    }
}