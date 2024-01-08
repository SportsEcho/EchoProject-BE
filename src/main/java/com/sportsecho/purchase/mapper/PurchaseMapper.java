package com.sportsecho.purchase.mapper;

import com.sportsecho.member.entity.Member;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseMapper {

    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    // 연관 관계가 복잡하여 사용하지 않음.. 추후 수정 가능성 있음..
//    PurchaseResponseDto toResponseDto(Purchase purchase);

    Purchase toEntity(PurchaseRequestDto requestDto, Member member);
}
