package com.sportsecho.member.controller;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(@Qualifier("V2") MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponseDto>> signup(
        @RequestBody MemberRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.of(
                "사용자 회원가입 성공",
                201,
                memberService.signup(request)
            )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
        @RequestBody MemberRequestDto request,
        HttpServletResponse response
    ) {
        memberService.login(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of(
                "사용자 로그인 성공",
                200,
                null
            )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        HttpServletRequest request
    ) {
        memberService.logout(memberDetails.getMember(), request);
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of(
                "사용자 로그아웃 성공",
                200,
                null
            )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> refresh(
        HttpServletRequest request
    ) {
        memberService.refresh(request);
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of(
                "갱신토큰 재발급 성공",
                200,
                null
            )
        );
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse<MemberResponseDto>> deleteMember(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of(
                "사용자 회원탈퇴 성공",
                200,
                memberService.deleteMember(memberDetails.getMember())
            )
        );
    }
}
