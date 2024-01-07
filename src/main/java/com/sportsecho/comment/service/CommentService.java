package com.sportsecho.comment.service;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.entity.Comment;
import com.sportsecho.comment.exception.CommentErrorCode;
import com.sportsecho.comment.repository.CommentRepository;
import com.sportsecho.game.entity.Game;
import com.sportsecho.game.repository.GameRepository;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
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
    // 댓글 추가
    @Transactional
    public CommentResponseDto addComment(Long gameId, CommentRequestDto commentDto, String userEmail) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new GlobalException(CommentErrorCode.GAME_NOT_FOUND));

        Member member = memberRepository.findByEmail(userEmail)
            .orElseThrow(() -> new GlobalException(CommentErrorCode.MEMBER_NOT_FOUND));

        Comment comment = Comment.builder()
            .content(commentDto.getContent())
            .game(game)
            .member(member)
            .build();

        commentRepository.save(comment);

        return new CommentResponseDto(comment.getId(), comment.getContent(), member.getMemberName(), comment.getCreatedAt());
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByGame(Long gameId) {
        List<Comment> comments = commentRepository.findByGameId(gameId);
        return comments.stream()
            .map(comment -> new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getMemberName(),
                comment.getCreatedAt()))
            .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentDto) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new GlobalException(CommentErrorCode.COMMENT_NOT_FOUND));

        comment.updateContent(commentDto.getContent());

        return new CommentResponseDto(
            comment.getId(),
            comment.getContent(),
            comment.getMember().getMemberName(),
            comment.getCreatedAt());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new GlobalException(CommentErrorCode.COMMENT_NOT_FOUND));

        commentRepository.delete(comment);
    }
}