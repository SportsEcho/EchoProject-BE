package com.sportsecho.memberProduct.controller;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.service.MemberProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberProductController {

    private final MemberProductService memberProductService;

    @PostMapping("/products/{productId}/carts")
    public ResponseEntity<ApiResponse<MemberProductResponseDto>> addCart(
        @PathVariable Long productId,
        @RequestBody MemberProductRequestDto requestDto,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        MemberProductResponseDto responseDto = memberProductService.addCart(productId, requestDto,
            memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("장바구니에 추가되었습니다.", 200, responseDto));
    }
}
