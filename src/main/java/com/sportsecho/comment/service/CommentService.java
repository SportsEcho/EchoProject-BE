package com.sportsecho.comment.service;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.entity.Comment;
import com.sportsecho.comment.repository.CommentRepository;
import com.sportsecho.game.entity.Game;
import com.sportsecho.game.repository.GameRepository;
import com.sportsecho.global.exception.ErrorCode;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, GameRepository gameRepository,
        MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
    }
    // 생성자 주입

    // 댓글 추가
    @Transactional
    public CommentResponseDto addComment(Long gameId, CommentRequestDto commentDto, String userEmail) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new GlobalException(ErrorCode.GAME_NOT_FOUND));

        Member member = memberRepository.findByEmail(userEmail)
            .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Comment comment = Comment.builder()
            .content(commentDto.getContent())
            .game(game)
            .memberName(member.getMemberName())
            .build();

        commentRepository.save(comment);

        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getMemberName(), comment.getCreatedAt());
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