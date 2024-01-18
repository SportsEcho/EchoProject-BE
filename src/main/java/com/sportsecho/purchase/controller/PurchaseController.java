package com.sportsecho.purchase.controller;

import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.service.PurchaseService;
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
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(@Qualifier("V1") PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseResponseDto> purchase(
        @RequestBody PurchaseRequestDto requestDto,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        PurchaseResponseDto responseDto = purchaseService.purchase(requestDto,
            memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void> cancelPurchase(
        @PathVariable Long purchaseId,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        purchaseService.cancelPurchase(purchaseId, memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponseDto>> getPurchaseList(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        List<PurchaseResponseDto> responseDtoList = purchaseService.getPurchaseList(
            memberDetails.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
