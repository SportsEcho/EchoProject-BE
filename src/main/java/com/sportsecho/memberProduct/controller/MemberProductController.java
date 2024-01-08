package com.sportsecho.memberProduct.controller;

import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.service.MemberProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class MemberProductController {

    private final MemberProductService memberProductService;

    public MemberProductController(@Qualifier("V1") MemberProductService memberProductService) {
        this.memberProductService = memberProductService;
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<MemberProductResponseDto> addCart(
        @PathVariable Long productId,
        @RequestBody MemberProductRequestDto requestDto,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        MemberProductResponseDto responseDto = memberProductService.addCart(productId, requestDto,
            memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<MemberProductResponseDto>> getCart(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        List<MemberProductResponseDto> responseDtoList = memberProductService.getCart(
            memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteCart(
        @PathVariable Long productId,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {

        memberProductService.deleteCart(productId, memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCart(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {

        memberProductService.deleteAllCart(memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
