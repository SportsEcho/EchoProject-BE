package com.sportsecho.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDto {
    private String memberName;
    private String email;
    private String password;
}