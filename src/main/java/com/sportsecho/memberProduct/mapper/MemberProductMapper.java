package com.sportsecho.memberProduct.mapper;

import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.entity.ProductImage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberProductMapper {

    MemberProductMapper INSTANCE = Mappers.getMapper(MemberProductMapper.class);

    @Mapping(target = "title", source = "product.title")
    @Mapping(target = "price", source = "product.price")
    MemberProductResponseDto toResponseDto(MemberProduct memberProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productsQuantity", source = "requestDto.productsQuantity")
    @Mapping(target = "member", source = "member")
    @Mapping(target = "product", source = "product")
    MemberProduct toEntity(MemberProductRequestDto requestDto, Member member, Product product);

    @AfterMapping
    default void toImageUrlList(MemberProduct memberProduct,
        @MappingTarget MemberProductResponseDto.MemberProductResponseDtoBuilder dtoBuilder) {

        dtoBuilder.imageUrlList(
            memberProduct.getProduct().getProductImageList().stream()
                .map(ProductImage::getImageUrl)
                .toList()
        );
    }

}
