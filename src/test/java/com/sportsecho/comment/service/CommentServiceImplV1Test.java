package com.sportsecho.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.entity.Comment;
import com.sportsecho.comment.repository.CommentRepository;
import com.sportsecho.game.entity.Game;
import com.sportsecho.game.repository.GameRepository;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class CommentServiceImplV1Test {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentServiceImplV1 commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("댓글 추가 테스트")
    void testAddComment() {
        // 가정(given)
        Long gameId = 1L;
        String userEmail = "user@example.com";
        String testCommentContent = "Test comment";

        CommentRequestDto commentDto = new CommentRequestDto();
        ReflectionTestUtils.setField(commentDto, "content", testCommentContent, String.class);

        Game game = new Game(); // Game 객체 생성
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        Member member = Member.builder()
            .memberName("Test User")
            .email(userEmail)
            .password("TestPass123!")
            .role(MemberRole.CUSTOMER)
            .build();
        when(memberRepository.findByEmail(userEmail)).thenReturn(Optional.of(member));

        // 실행(when)
        CommentResponseDto result = commentService.addComment(gameId, commentDto, userEmail);

        // 검증(then)
        assertNotNull(result);
        assertEquals(commentDto.getContent(), result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}