package com.sportsecho.game.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameRequestDto {
    private String sportType;
    private String teamA;
    private String teamB;
    private LocalDateTime gameDateTime;
    private String location;
}

