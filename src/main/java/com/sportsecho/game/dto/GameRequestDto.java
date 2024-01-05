package com.sportsecho.game.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameRequestDto {
    private String sportType;
    private String teamA;
    private String teamB;
    private LocalDateTime gameDateTime;
    private String location;
}

