package com.sportsecho.game.controller;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.common.dto.ResponseCode;
import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.service.GameService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{sportType}")
    public ResponseEntity<ApiResponse<List<GameResponseDto>>> getGamesBySport(@PathVariable String sportType) {
        ApiResponse<List<GameResponseDto>> response = gameService.getGamesBySport(sportType);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

