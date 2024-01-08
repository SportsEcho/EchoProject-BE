package com.sportsecho.purchase.mapper;

import com.sportsecho.member.entity.Member;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseMapper {

    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

//    PurchaseResponseDto toResponseDto(Purchase purchase);

    Purchase toEntity(PurchaseRequestDto requestDto, Member member);
}
