package com.sportsecho.purchase.mapper;

import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.member.entity.Member;
import com.sportsecho.product.entity.Product;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.entity.Purchase;
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
    @Mapping(target = "purchaseProductList", ignore = true)
    Purchase fromPurchaseRequestDto(PurchaseRequestDto requestDto, Member member);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", source = "discountedPrice")
    @Mapping(target = "address", source = "requestDto.address")
    @Mapping(target = "phone", source = "requestDto.phone")
    @Mapping(target = "member", source = "member")

    @Mapping(target = "purchaseProductList", ignore = true)
    Purchase fromPurchaseHotdealReqeustDto(PurchaseHotdealRequestDto requestDto, int discountedPrice,
        Member member);


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
