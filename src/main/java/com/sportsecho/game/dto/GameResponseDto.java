package com.sportsecho.game.dto;

import java.time.LocalDateTime;
public record GameResponseDto(
    String sportType,
    String teamA,
    String teamB,
    LocalDateTime gameDateTime,
    String location
) {}
