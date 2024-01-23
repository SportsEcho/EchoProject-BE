package com.sportsecho.game.dto;

import com.sportsecho.game.entity.SportsType;
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

    private Long id;
    private String homeTeamName;
    private String homeTeamLogo;
    private String awayTeamName;
    private String awayTeamLogo;
    private String leagueLogo;
    private LocalDateTime date;
    private String venueName;
    private String homeGoal;
    private String awayGoal;
    private SportsType sportsType;

}
