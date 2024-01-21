package com.sportsecho.gamechat.dto;

public class GameChatResponseDto {

    private String content;

    public GameChatResponseDto() {
    }

    public GameChatResponseDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}