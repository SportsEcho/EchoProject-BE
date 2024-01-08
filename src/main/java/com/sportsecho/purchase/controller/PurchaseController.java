package com.sportsecho.purchase.controller;

import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.service.PurchaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(@Qualifier("V1") PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/carts/purchase")
    public ResponseEntity<PurchaseResponseDto> purchase(
        @RequestBody PurchaseRequestDto requestDto,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {

        PurchaseResponseDto responseDto = purchaseService.purchase(
            requestDto, memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
