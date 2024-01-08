package com.sportsecho.comment.service;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(Long gameId, CommentRequestDto commentDto, String userEmail);
    List<CommentResponseDto> getCommentsByGame(Long gameId);
    CommentResponseDto updateComment(Long commentId, CommentRequestDto commentDto, String userEmail);
    void deleteComment(Long commentId, String userEmail);
}
