package com.sportsecho.member.controller;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.service.MemberServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Response 방식 확정되면 Response 수정
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberServiceImpl memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto request) {
        memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody MemberRequestDto request, HttpServletResponse response
    ) {
        memberService.login(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        memberService.deleteMember(memberDetails.getMember());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
