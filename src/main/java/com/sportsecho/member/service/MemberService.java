package com.sportsecho.member.service;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    /**
     * Member 회원가입
     * @param request Member 회원가입 요청 정보
     * @return Member 회원가입 정보
     * */
    MemberResponseDto signup(MemberRequestDto request, MemberRole role);

    /**
     * Member 관리자 회원가입
     * @param request Member 관리자 회원가입 요청 정보
     * @param key 관리자 회원가입을 위한 유니크 키값
     * @return Member 관리자 회원가입 정보
     * */
    MemberResponseDto adminSignup(MemberRequestDto request, String key);

    /**
     * Member 로그인
     * @param response Jwt 토큰을 담아서 전달하기 위한 HttpServletResponse
     * @param request Member 로그인 요청 정보
     * */
    void login(MemberRequestDto request, HttpServletResponse response);

    /**
     * Member 로그아웃
     * @param member 로그아웃 요청 Member
     * @param request 사용자의 RefreshToken을 전달받기 위한 HttpServletRequest
     * */
    void logout(Member member, HttpServletRequest request);

    /**
     * RefreshToken 재발급
     * @param request 사용자의 RefreshToken을 전달받기 위한 HttpServletRequest
     * */
    void refresh(HttpServletRequest request, HttpServletResponse response);

    /**
     * Member 정보 조회
     * @param member 조회할 Member 객체
     * */
    MemberResponseDto readMember(Member member);

    /**
     * Member 삭제
     * @param member 삭제할 Member 객체
     * @return 삭제된 Member 정보
     * */
    MemberResponseDto deleteMember(Member member);

    /**
     * Mamber Kakao 로그인
     * @param code kakao 로그인 요청시 전달받은 인가 code
     * @param response Jwt 토큰을 담아서 전달하기 위한 HttpServletResponse
     * */
    void kakaoLogin(String code, HttpServletResponse response);

    /**
     * Member Naver 로그인
     * @param code naver 로그인 요청시 전달받은 인가 code
     * @param response Jwt 토큰을 담아서 전달하기 위한 HttpServletResponse
     * */
    void naverLogin(String code, HttpServletResponse response);

    /**
     * Member Google 로그인
     * @param code google 로그인 요청시 전달받은 인가 code
     * @param response Jwt 토큰을 담아서 전달하기 위한 HttpServletResponse
     * */
    void googleLogin(String code, HttpServletResponse response);
}
