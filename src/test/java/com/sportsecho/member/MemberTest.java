package com.sportsecho.member;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.entity.Member;

public interface MemberTest {

    String TEST_MEMBER_NAME = "testMemberName";
    String TEST_EMAIL = "test_email@echo.com";
    String TEST_PASSWORD = "test_password1A~";

    String ANOTHER_TEST_EMAIL = "test_email@echo.net";
    String ANOTHER_TEST_PASSWORD = "test_password1A!";

    String TEST_REFRESH_TOKEN = "test_refresh_token";

    Member TEST_MEMBER = Member.builder()
        .memberName(TEST_MEMBER_NAME)
        .email(TEST_EMAIL)
        .password(TEST_PASSWORD)
        .build();

    MemberRequestDto TEST_MEMBER_REQUEST_DTO = MemberTestUtil.getTestMemberRequestDto(
        TEST_MEMBER_NAME,
        TEST_EMAIL,
        TEST_PASSWORD
    );
}
