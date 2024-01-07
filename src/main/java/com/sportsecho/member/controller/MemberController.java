package com.sportsecho.member.controller;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.common.dto.ResponseCode;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.service.MemberServiceImplV2;
import jakarta.servlet.http.HttpServletRequest;
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
            ApiResponse.of(
                ResponseCode.CREATED,
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
                ResponseCode.OK,
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
                ResponseCode.OK,
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
                ResponseCode.OK,
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
                ResponseCode.OK,
                memberService.deleteMember(memberDetails.getMember())
            )
        );
    }
}
