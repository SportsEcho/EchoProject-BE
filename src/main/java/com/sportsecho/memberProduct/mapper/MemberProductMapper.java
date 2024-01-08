package com.sportsecho.memberProduct.mapper;

import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberProductMapper {

    MemberProductMapper INSTANCE = Mappers.getMapper(MemberProductMapper.class);

    MemberProductResponseDto toResponseDto(MemberProduct memberProduct);

    MemberProduct toEntity(MemberProductRequestDto requestDto, Member member, Product product);
}
