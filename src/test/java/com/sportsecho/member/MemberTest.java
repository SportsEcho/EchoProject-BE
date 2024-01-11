package com.sportsecho.member;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.entity.Member;

public interface MemberTest {

    Member TEST_MEMBER = Member.builder()
        .memberName("test_member_name")
        .email("test_email@echo.com")
        .password("test_password1A~")
        .build();

    MemberRequestDto TEST_MEMBER_REQUEST_DTO = MemberTestUtil.getTestMemberRequestDto(
        "test_member_name",
        "test_email@echo.com",
        "test_password1A~"
    );
}
