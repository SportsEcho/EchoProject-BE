package com.sportsecho.game.dto;

import java.util.Map;
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
public class GameResponseDto {
    private String sportType;
    private String teamA;
    private String teamB;
    private LocalDateTime gameDateTime;
    private String location;

    public static GameResponseDto fromMap(Map<String, Object> gameData) {
        return GameResponseDto.builder()
            .sportType((String) gameData.get("sportType"))
            .teamA((String) gameData.get("teamA"))
            .teamB((String) gameData.get("teamB"))
            .gameDateTime(LocalDateTime.parse((String) gameData.get("gameDateTime")))
            .location((String) gameData.get("location"))
            .build();
    }
}

