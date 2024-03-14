package com.sportsecho.gamechat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private Long roomId;
    private String sender;
    private String message;
    private String sendTime;
}