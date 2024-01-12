package com.sportsecho.purchaseProduct.mapper;

import com.sportsecho.purchaseProduct.dto.PurchaseProductResponseDto;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseProductMapper {

    PurchaseProductMapper INSTANCE = Mappers.getMapper(PurchaseProductMapper.class);

    @Mapping(target = "title", source = "product.title")
    @Mapping(target = "price", source = "product.price")
    PurchaseProductResponseDto toResponseDto(PurchaseProduct purchaseProduct);
}
