package com.sportsecho.gamechat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
    private String type;
    private String message;
    private Long roomId;
    private Long userId;
}
