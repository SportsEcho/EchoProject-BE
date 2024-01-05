package com.sportsecho.game.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDto {
    private String sportType;
    private String teamA;
    private String teamB;
    private LocalDateTime gameDateTime;
    private String location;

}
