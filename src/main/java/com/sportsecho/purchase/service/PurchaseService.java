package com.sportsecho.purchase.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;

public interface PurchaseService {

    /***
     * 구매 API
     * @param requestDto 배송지 정보
     * @param member 유저 정보
     * @return 배송지, 상품 정보
     */
    PurchaseResponseDto purchase(PurchaseRequestDto requestDto, Member member);
}
