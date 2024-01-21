package com.sportsecho.gamechat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameChatRequestDto {

    private String message;

    public GameChatRequestDto(String message) {
        this.message = message;
    }
}