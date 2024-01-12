package com.sportsecho.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.common.jwt.exception.JwtErrorCode;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.exception.MemberErrorCode;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.member.service.MemberServiceImplV2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberIntegrationTest implements MemberTest {

    @Autowired
    MemberServiceImplV2 memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtUtil jwtUtil;

    @BeforeAll
    void setUpAll() {
        //member 회원가입을 통한 테스트 데이터 생성 - 회원가입 테스트는 Unit Test에서 진행
        memberService.signup(TEST_MEMBER_REQUEST_DTO, MemberRole.CUSTOMER);
    }

    @Nested
    @DisplayName("Member 로그인 테스트")
    class memberLoginTest {

        @Test
        @DisplayName("Member 로그인 테스트 성공 - 토큰 반환")
        void memberLogin_success() {
            //given
            MockHttpServletResponse response = new MockHttpServletResponse();

            //when
            memberService.login(TEST_MEMBER_REQUEST_DTO, response);

            String accessToken = response.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            String refreshToken = response.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //then
            assertNotNull(accessToken);
            assertNotNull(refreshToken);
        }

        @Test
        @DisplayName("Member 로그인 테스트 실패 - 존재하지 않는 이메일")
        void memberLogin_fail_memberNotFound() {
            //given
            MockHttpServletResponse response = new MockHttpServletResponse();

            MemberRequestDto requestDto = MemberTestUtil.getTestMemberRequestDto(
                ANOTHER_TEST_EMAIL,
                TEST_MEMBER.getPassword()
            );

            //when
            GlobalException exception = assertThrows(GlobalException.class, () ->
                memberService.login(requestDto, response)
            );

            //then
            assertEquals(MemberErrorCode.INVALID_AUTH, exception.getErrorCode());
        }

        @Test
        @DisplayName("Member 로그인 테스트 실패 - 비밀번호 불일치")
        void memberLogin_fail_invalidPassword() {
            //given
            MockHttpServletResponse response = new MockHttpServletResponse();

            MemberRequestDto requestDto = MemberTestUtil.getTestMemberRequestDto(
                TEST_MEMBER.getEmail(),
                ANOTHER_TEST_PASSWORD
            );

            //when
            GlobalException exception = assertThrows(GlobalException.class, () ->
                memberService.login(requestDto, response)
            );

            //then
            assertEquals(MemberErrorCode.INVALID_AUTH, exception.getErrorCode());
        }

        @Test
        @DisplayName("Member 로그인 테스트 성공 - 토큰 이메일 검증")
        void memberLogin_tokenValidate_success() {
            //given
            MockHttpServletResponse response = new MockHttpServletResponse();

            //when
            memberService.login(TEST_MEMBER_REQUEST_DTO, response);

            String accessToken = response.getHeader(JwtUtil.AUTHORIZATION_HEADER);

            String email;

            if(accessToken != null) {
                email = jwtUtil.getSubject(jwtUtil.substringToken(accessToken));
            } else {
                throw new GlobalException(JwtErrorCode.ACCESS_TOKEN_NOT_FOUND);
            }

            //then
            assertEquals(TEST_MEMBER_REQUEST_DTO.getEmail(), email);
        }
    }

    @Nested
    @DisplayName("Member 로그아웃 테스트")
    class MemberLogoutTest {

    }

}