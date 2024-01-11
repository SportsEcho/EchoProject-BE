package com.sportsecho.member;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberTestUtil {

    public static Member getTestMember(String memberName, String email, String password, MemberRole role) {
        return Member.builder()
            .memberName(memberName)
            .email(email)
            .password(password)
            .role(role)
            .build();
    }

    public static MemberRequestDto getTestMemberRequestDto(String memberName, String email, String password) {
        MemberRequestDto memberRequestDto = new MemberRequestDto();

        ReflectionTestUtils.setField(memberRequestDto, "memberName", memberName, String.class);
        ReflectionTestUtils.setField(memberRequestDto, "email", email, String.class);
        ReflectionTestUtils.setField(memberRequestDto, "password", password, String.class);

        return memberRequestDto;
    }

}
