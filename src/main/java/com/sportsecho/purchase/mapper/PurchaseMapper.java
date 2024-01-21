package com.sportsecho.purchase.mapper;

import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.entity.Purchase;
import com.sportsecho.purchaseProduct.mapper.PurchaseProductMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseMapper {

    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    @Mapping(target = "purchaseDate", source = "createdAt")
    PurchaseResponseDto toResponseDto(Purchase purchase);

    @Mapping(target = "totalPrice", constant = "0")
    @Mapping(target = "member", source = "member")
    Purchase toEntity(PurchaseRequestDto requestDto, Member member);

    @Mapping(target = "totalPrice", constant = "0")
    @Mapping(target = "member", source = "member")
    Purchase toEntity(PurchaseHotdealRequestDto requestDto, Member member);

    @AfterMapping
    default void toPurchaseProductList(Purchase purchase,
        @MappingTarget PurchaseResponseDto.PurchaseResponseDtoBuilder dtoBuilder) {

        dtoBuilder.purchaseProductList(
            purchase.getPurchaseProductList().stream()
                .map(PurchaseProductMapper.INSTANCE::toResponseDto)
                .toList()
        );
    }
}
