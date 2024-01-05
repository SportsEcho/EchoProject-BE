package com.sportsecho.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameRequestDto {
    private String sportType;
    private String teamA;
    private String teamB;
    private LocalDateTime gameDateTime;
    private String location;
}

