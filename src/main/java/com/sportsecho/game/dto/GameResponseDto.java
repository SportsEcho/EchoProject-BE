package com.sportsecho.game.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameResponseDto {
    private Long id;
    private String sportType;
    private String teamA;
    private String teamB;
    private LocalDateTime matchDateTime;
    private String location;
    // 댓글 관련 필드 추가 예정
}
