package com.sportsecho.comment.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.entity.Comment;
import com.sportsecho.comment.exception.CommentErrorCode;
import com.sportsecho.comment.repository.CommentRepository;
import com.sportsecho.game.GameTestUtil;
import com.sportsecho.game.entity.Game;
import com.sportsecho.game.repository.GameRepository;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.MemberTestUtil;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CommentTestIntegration {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Game testGame;
    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = memberRepository.save(
            MemberTestUtil.getTestMember("Test User", "user@example.com", "password",
                MemberRole.CUSTOMER)
        );

        testGame = gameRepository.save(
            GameTestUtil.createTestGame()
        );
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        gameRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 추가 테스트")
    void testAddComment() {
        CommentRequestDto commentDto = new CommentRequestDto("Test comment");


        commentService.addComment(testGame.getId(), commentDto, testMember.getEmail());

        List<Comment> comments = commentRepository.findByGameId(testGame.getId());
        assertFalse(comments.isEmpty());
        assertEquals("Test comment", comments.get(0).getContent());
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    void testGetCommentsByGame() {
        testAddComment();

        List<Comment> comments = commentRepository.findByGameId(testGame.getId());
        assertFalse(comments.isEmpty());
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void testUpdateComment() {
        testAddComment();
        Comment existingComment = commentRepository.findByGameId(testGame.getId()).get(0);
        CommentRequestDto updatedDto = new CommentRequestDto("Updated content");


        commentService.updateComment(existingComment.getId(), updatedDto, testMember.getEmail());

        Comment updatedComment = commentRepository.findById(existingComment.getId()).orElseThrow();
        assertEquals("Updated content", updatedComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void testDeleteComment() {
        testAddComment();

        Comment existingComment = commentRepository.findByGameId(testGame.getId()).get(0);
        commentService.deleteComment(existingComment.getId(), testMember.getEmail());

        assertTrue(commentRepository.findById(existingComment.getId()).isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제 시도 테스트")
    void testDeleteNonExistentComment() {
        Long nonExistentCommentId = 999L;

        GlobalException exception = assertThrows(GlobalException.class, () ->
            commentService.deleteComment(nonExistentCommentId, testMember.getEmail()));

        assertEquals(CommentErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }
}