package com.sportsecho.comment.controller;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.service.CommentService;
import com.sportsecho.comment.service.CommentServiceImplV1;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games/{gameId}")
public class CommentController {

    private final CommentService commentService;

    public CommentController(@Qualifier("commentServiceImplV1")CommentService commentService) {
        this.commentService = commentService;
    }


    // 게임별 댓글 추가
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> addComment(
        @PathVariable Long gameId,
        @RequestBody CommentRequestDto commentDto,
        Authentication authentication) {

        String userEmail = authentication.getName();
        CommentResponseDto responseDto = commentService.addComment(gameId, commentDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 게임별 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByGame(@PathVariable Long gameId) {
        List<CommentResponseDto> comments = commentService.getCommentsByGame(gameId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
        @PathVariable Long commentId,
        @RequestBody CommentRequestDto commentDto,
        Authentication authentication) {

        String userEmail = authentication.getName();
        CommentResponseDto responseDto = commentService.updateComment(commentId, commentDto, userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long commentId,
        Authentication authentication) {

        String userEmail = authentication.getName();
        commentService.deleteComment(commentId, userEmail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}