package com.sportsecho.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.common.jwt.exception.JwtErrorCode;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.exception.MemberErrorCode;
import com.sportsecho.member.service.MemberService;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberIntegrationTest implements MemberTest {

    @Autowired
    @Qualifier("V2")
    MemberService memberService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisUtil redisUtil;

    @BeforeAll
    void setUpAll() {
        //member 회원가입을 통한 테스트 데이터 생성 - 회원가입 테스트는 Unit Test에서 진행
        memberService.signup(TEST_MEMBER_REQUEST_DTO, MemberRole.CUSTOMER);
    }

    @Nested
    @DisplayName("Member 로그인 테스트")
    class memberLoginTest {

        MockHttpServletResponse response = new MockHttpServletResponse();

        @Test
        @DisplayName("Member 로그인 테스트 성공 - 토큰 반환")
        void memberLogin_success() {
            //given

            //when
            memberService.login(TEST_MEMBER_REQUEST_DTO, response);

            String accessToken = response.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            String refreshToken = response.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //then
            assertNotNull(accessToken);
            assertNotNull(refreshToken);
        }

        @Test
        @DisplayName("Member 로그인 테스트 성공 - 토큰 이메일 검증")
        void memberLogin_tokenValidate_success() {
            //given
            String email;

            //when
            memberService.login(TEST_MEMBER_REQUEST_DTO, response);

            String accessToken = response.getHeader(JwtUtil.AUTHORIZATION_HEADER);

            if(accessToken != null) {
                email = jwtUtil.getSubject(jwtUtil.substringToken(accessToken));
            } else {
                throw new GlobalException(JwtErrorCode.ACCESS_TOKEN_NOT_FOUND);
            }

            //then
            assertEquals(TEST_MEMBER_REQUEST_DTO.getEmail(), email);
        }

        @Test
        @DisplayName("Member 로그인 테스트 성공 - 갱신 토큰 저장")
        void memberLogin_refreshToken_create_success() {
            //given

            //when
            memberService.login(TEST_MEMBER_REQUEST_DTO, response);

            String refreshToken = response.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //then
            assertTrue(redisUtil.isExist(refreshToken));
        }

        @Test
        @DisplayName("Member 로그인 테스트 실패 - 존재하지 않는 이메일")
        void memberLogin_fail_memberNotFound() {
            //given
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
    }

    @Nested
    @DisplayName("Member 로그아웃 테스트")
    class MemberLogoutTest {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        @BeforeEach
        void MemberLogoutTestSetUp() {
            memberService.login(TEST_MEMBER_REQUEST_DTO, response);

            request.addHeader(JwtUtil.AUTHORIZATION_HEADER,
                Objects.requireNonNull(response.getHeader(JwtUtil.AUTHORIZATION_HEADER)));

            request.addHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER,
                Objects.requireNonNull(response.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER)));
        }

        @Test
        @DisplayName("Member 로그아웃 테스트 성공 - 갱신 토큰 삭제")
        void memberLogout_success() {
            //given
            String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //when
            memberService.logout(TEST_MEMBER, request);

            //then
            assertFalse(redisUtil.isExist(refreshToken));
        }

        @Test
        @DisplayName("Member 로그아웃 테스트 실패 - 갱신 토큰이 존재하지 않는 경우")
        void memberLogout_fail_refreshTokenNotFound() {
            //given
            String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //when
            redisUtil.removeToken(refreshToken);

            GlobalException exception = assertThrows(GlobalException.class, () ->
                memberService.logout(TEST_MEMBER, request)
            );

            //then
            assertEquals(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("Member 로그아웃 테스트 실패 - 현재 접속중인 사용자가 로그아웃을 요청한 사용자가 아닌 경우")
        void memberLogout_fail_invalidRequestMember() {
            //given
            Member anoterTestMember = MemberTestUtil.getTestMember(
                ANOTHER_TEST_EMAIL,
                TEST_MEMBER.getPassword()
            );

            //when
            GlobalException exception = assertThrows(GlobalException.class, () ->
                memberService.logout(anoterTestMember, request)
            );

            //then
            assertEquals(MemberErrorCode.INVALID_REQUEST, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Member 접근 토큰 재발급 테스트")
    class MemberRefreshTest {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        @BeforeEach
        void MemberRefreshSetUp() {
            memberService.login(TEST_MEMBER_REQUEST_DTO, response);

            request.addHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER,
                Objects.requireNonNull(response.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER)));
        }

        @Test
        @DisplayName("Member 접근 토큰 재발급 테스트 성공")
        void memberRefresh_success() {
            //given

            //when
            memberService.refresh(request, response);

            String accessToken = response.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            String refreshToken = response.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //then
            assertNotNull(accessToken);
            assertNotNull(refreshToken);
        }

        @Test
        @DisplayName("Member 접근 토큰 재발급 테스트 실패 - 갱신 토큰이 존재하지 않는 경우")
        void memberRefresh_fail_refreshTokenNotFound() {
            //given
            String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //when
            redisUtil.removeToken(refreshToken);

            GlobalException exception = assertThrows(GlobalException.class, () ->
                memberService.refresh(request, response)
            );

            //then
            assertEquals(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("Member 접근 토큰 재발급 테스트 실패 - 사용자가 존재하지 않는 경우")
        void memberRefresh_fail_memberNotFound() {
            //given
            String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);

            //when
            redisUtil.saveRefreshToken(refreshToken, ANOTHER_TEST_EMAIL);

            GlobalException exception = assertThrows(GlobalException.class, () ->
                memberService.refresh(request, response)
            );

            //then
            assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Member 회원탈퇴 테스트")
    class MemberDeleteTest {

        @Test
        @DisplayName("Member 회원탈퇴 테스트 성공")
        void memberDelete_success() {
            //given

            //when
            MemberResponseDto memberResponseDto = memberService.deleteMember(TEST_MEMBER);

            //then
            assertEquals(TEST_MEMBER.getEmail(), memberResponseDto.getEmail());
        }
    }

}