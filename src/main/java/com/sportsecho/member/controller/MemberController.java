package com.sportsecho.member.controller;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.service.MemberServiceImplV1;
import com.sportsecho.member.service.MemberServiceImplV2;
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

    private final MemberServiceImplV2 memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponseDto>> signup(
        @RequestBody MemberRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            memberService.signup(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
        @RequestBody MemberRequestDto request, HttpServletResponse response
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            memberService.login(request, response)
        );
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse<MemberResponseDto>> deleteMember(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        memberService.deleteMember(memberDetails.getMember());
        return ResponseEntity.status(HttpStatus.OK).body(
            memberService.deleteMember(memberDetails.getMember())
        );
    }
}
