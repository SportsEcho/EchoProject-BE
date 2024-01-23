package com.sportsecho.gamechat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameChatResponseDto {

    private String sender;
    private String content;
    private String sendAt;

    public String getContent() {
        return content;
    }

}