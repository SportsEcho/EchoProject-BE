package com.sportsecho.game.controller;

import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.service.CommentService;
import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.service.GameService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;
    private final CommentService commentService;

    @Autowired
    public GameController(GameService gameService, CommentService commentService) {
        this.gameService = gameService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<GameResponseDto>> getGamesBySport(@RequestParam String sportType) {
        List<GameResponseDto> games = gameService.getGamesBySport(sportType);
        return ResponseEntity.status(HttpStatus.OK).body(games);
    }

    // 경기별 댓글 조회 API
    @GetMapping("/{gameId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByGame(@PathVariable Long gameId) {
        List<CommentResponseDto> comments = commentService.getCommentsByGame(gameId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
}


