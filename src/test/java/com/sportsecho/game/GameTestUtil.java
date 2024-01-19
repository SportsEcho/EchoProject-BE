package com.sportsecho.game;

import com.sportsecho.game.entity.Game;
import java.time.LocalDateTime;

public class GameTestUtil {
    public static Game createTestGame() {
        return Game.builder()
            .homeTeamName("homeTeamName")
            .homeTeamLogo("homeTeamLogo")
            .awayTeamName("awayTeamName")
            .awayTeamLogo("awayTeamLogo")
            .leagueLogo("leagueLogo")
            .date(LocalDateTime.now())
            .venueName("venueName")
            .homeGoal(3)
            .awayGoal(4)
            .build();
    }

}
