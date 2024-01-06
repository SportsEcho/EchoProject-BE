package com.sportsecho.comment.service;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.repository.CommentRepository;
import com.sportsecho.game.entity.Game;
import com.sportsecho.game.repository.GameRepository;
import com.sportsecho.global.exception.ErrorCode;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.repository.MemberRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    // 생성자 주입

    // 댓글 추가
    public Comment addComment(Long gameId, CommentRequestDto commentDto) {
        // 게임 조회, 댓글 생성 로직
    }

    // 댓글 조회
    public List<CommentResponseDto> getCommentsByGame(Long gameId) {
        // 게임별 댓글 조회 로직
    }

    // 댓글 수정
    public Comment updateComment(Long commentId, CommentRequestDto commentDto) {
        // 댓글 수정 로직
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        // 댓글 삭제 로직
    }
}