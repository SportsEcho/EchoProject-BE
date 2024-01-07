package com.sportsecho.memberProduct.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;

public interface MemberProductService {

    /**
     * 장바구니 추가 API
     *
     * @param productId  상품 Id
     * @param requestDto 상품 수량 정보
     * @param member     유저 정보
     * @return 상품 정보
     */
    MemberProductResponseDto addCart(Long productId, MemberProductRequestDto requestDto,
        Member member);

    /***
     * 장바구니 상품 삭제 API
     * @param productId 상품 Id
     * @param member 유저 정보
     */
    void deleteCart(Long productId, Member member);

    /***
     * 장바구니 비우기 API
     * @param member 유저 정보
     */
    void deleteAllCart(Member member);
}
