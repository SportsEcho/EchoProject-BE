package com.sportsecho.purchase.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import java.util.List;

public interface PurchaseService {

    /***
     * 구매 API
     * @param requestDto 배송지 정보
     * @param member 유저 정보
     * @return 배송지, 상품 정보
     */
    PurchaseResponseDto purchase(PurchaseRequestDto requestDto, Member member);

    /***
     * 구매 목록 조회 API
     * @param member 유저 정보
     * @return 구매 상품 목록
     */
    List<PurchaseResponseDto> getPurchaseList(Member member);

    /***
     *
     * @param purchaseId
     * @param member
     */
    void cancelPurchase(Long purchaseId, Member member);
}
