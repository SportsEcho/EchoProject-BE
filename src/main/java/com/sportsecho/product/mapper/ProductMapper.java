package com.sportsecho.product.mapper;

import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.dto.response.ProductResponseDto;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.entity.ProductImage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResponseDto toResponseDto(Product product);

    @AfterMapping
    default void toImageUrlList(Product product,
        @MappingTarget ProductResponseDto.ProductResponseDtoBuilder dtoBuilder) {

        dtoBuilder.imageUrlList(
            product.getProductImageList().stream()
                .map(ProductImage::getImageUrl)
                .toList()
        );
    }

    Product toEntity(ProductRequestDto requestDto);

}
