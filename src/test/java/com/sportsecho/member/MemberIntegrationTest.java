package com.sportsecho.member;

import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.service.MemberServiceImplV2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class MemberIntegrationTest implements MemberTest {

    @Autowired
    MemberServiceImplV2 memberService;

//    @Nested
//    @DisplayName("Member 회원가입 테스트")
//    @Rollback(false)
//    class memberSignupTest {
//
//        @Test
//        void memberSignup_success() {
//            //given
//
//            //when
//            memberService.signup(TEST_MEMBER_REQUEST_DTO, MemberRole.CUSTOMER);
//
//            //then
//        }
//
//    }
//
//    @Nested
//    @DisplayName("Member 로그인 테스트")
//    class memberLoginTest {
//
//        @Test
//        @DisplayName("Member 로그인 테스트 성공")
//        void memberLogin_success() {
//            //given
//            MockHttpServletResponse response = new MockHttpServletResponse();
//
//            //when
//            memberService.login(TEST_MEMBER_REQUEST_DTO, response);
//
//            //then
//        }
//    }
}