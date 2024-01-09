package com.sportsecho.member.controller;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(
        @Qualifier("V2") MemberService memberService
    ) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(
        @Valid @RequestBody MemberRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                memberService.signup(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
        @RequestBody MemberRequestDto request,
        HttpServletResponse response
    ) {
        memberService.login(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        HttpServletRequest request
    ) {
        memberService.logout(memberDetails.getMember(), request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        memberService.refresh(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("")
    public ResponseEntity<MemberResponseDto> deleteMember(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            memberService.deleteMember(memberDetails.getMember())
        );
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoLogin(
        @RequestParam("code") String code,
        HttpServletResponse response
    ) {
        memberService.kakaoLogin(code, response);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<Void> naverLogin(
        @RequestParam("code") String code,
        HttpServletResponse response
    ) {
        memberService.naverLogin(code, response);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
