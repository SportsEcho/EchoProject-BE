package com.sportsecho.game.controller;

import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(@Qualifier("gameServiceImplV1") GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/by-league")
    public ResponseEntity<List<GameResponseDto>> getGamesByDateAndLeague(
        @RequestParam String date,
        @RequestParam String league
    ) {
        List<GameResponseDto> games = gameService.getGamesByDateAndLeague(date, league);
        return ResponseEntity.ok(games);
    }
}
