package com.sportsecho.game;

import com.sportsecho.game.entity.Game;
import java.time.LocalDateTime;

public class GameTestUtil {
    public static Game createTestGame(String sportType, String teamA, String teamB, LocalDateTime dateTime, String location) {
        return Game.builder()
            .sportType(sportType)
            .teamA(teamA)
            .teamB(teamB)
            .gameDateTime(dateTime)
            .location(location)
            .build();
    }

}
