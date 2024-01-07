package com.sportsecho.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 200 OK
    OK(200, "요청성공"),

    // 201 CREATED
    CREATED(201, "생성성공")


    ;
    private final int httpstatus;
    private final String message;
}