package com.sportsecho.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.common.oauth.OAuthUtil;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.MemberTest;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.exception.MemberErrorCode;
import com.sportsecho.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest implements MemberTest {

    @InjectMocks
    MemberServiceImplV2 memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    OAuthUtil oAuthUtil;

    @Mock
    RedisUtil redisUtil;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("Member 회원가입 테스트")
    class memberSignupTest {

        @Test
        @DisplayName("Member 회원가입 테스트 성공")
        void memberSignup_success() {
            //given
            given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

            //when
            MemberResponseDto response = memberService.signup(TEST_MEMBER_REQUEST_DTO, MemberRole.CUSTOMER);

            //then
            assertEquals(TEST_MEMBER_REQUEST_DTO.getMemberName(), response.getMemberName());
            assertEquals(TEST_MEMBER_REQUEST_DTO.getEmail(), response.getEmail());
        }

        @Test
        @DisplayName("Member 회원가입 테스트 실패 - 회원가입 요청 이메일 중복")
        void memberSignup_fail_duplicate_email() {
            //given
            given(memberRepository.findByEmail(any())).willReturn(Optional.of(TEST_MEMBER));

            //when
            GlobalException exception = assertThrows(GlobalException.class, () ->
                memberService.signup(TEST_MEMBER_REQUEST_DTO, MemberRole.CUSTOMER)
            );

            //then
            assertEquals(MemberErrorCode.DUPLICATED_EMAIL, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Member 로그인 테스트")
    class memberLoginTest {

        @Test
        @DisplayName("Member 로그인 테스트 성공")
        void memberLogin_success() {
            //given
            MockHttpServletResponse response = new MockHttpServletResponse();

            //when

            //then
        }
    }
}