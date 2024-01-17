package com.sportsecho.hotdeal.service;

import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.PurchaseHotdealReqeustDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.dto.response.PurchaseHotdealResponseDto;
import com.sportsecho.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface HotdealService {

    HotdealResponseDto createHotdeal(Member member, Long productId, HotdealRequestDto requestDto);

    List<HotdealResponseDto> getHotdealList(Pageable pageable);

    HotdealResponseDto getHotdeal(Long hotdealId);

    HotdealResponseDto updateHotdeal(Member member, Long hotdealId, UpdateHotdealInfoRequestDto requestDto);

    void deleteHotdeal(Member member, Long hotdealId);

    PurchaseHotdealResponseDto purchaseHotdeal(Member member, PurchaseHotdealReqeustDto requestDto);
}
