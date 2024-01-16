package com.sportsecho.hotdeal.service;

import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface HotdealService {

    HotdealResponseDto createHotdeal(Member member, Long productId, HotdealRequestDto requestDto);

    List<HotdealResponseDto> getHotdealList(Pageable pageable);

    HotdealResponseDto getHotdeal(Long hotdealId);

    HotdealResponseDto updateHotdeal(Member member, Long hotdealId, UpdateHotdealInfoRequestDto requestDto);

    void decreaseHotdealDealQuantity(Member member, Long hotdealId ,int quantity);

    void deleteHotdeal(Member member, Long hotdealId);
}
