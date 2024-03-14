package com.sportsecho.gamechat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
    private Long roomId;
    private String sender;
    private String message;
}