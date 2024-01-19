package com.sportsecho.memberProduct.mapper;

import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto.MemberProductResponseDtoBuilder;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.entity.MemberProduct.MemberProductBuilder;
import com.sportsecho.product.entity.Product;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-19T13:03:45+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class MemberProductMapperImpl implements MemberProductMapper {

    @Override
    public MemberProductResponseDto toResponseDto(MemberProduct memberProduct) {
        if ( memberProduct == null ) {
            return null;
        }

        MemberProductResponseDtoBuilder memberProductResponseDto = MemberProductResponseDto.builder();

        memberProductResponseDto.title( memberProductProductTitle( memberProduct ) );
        memberProductResponseDto.price( memberProductProductPrice( memberProduct ) );
        memberProductResponseDto.id( memberProduct.getId() );
        memberProductResponseDto.productsQuantity( memberProduct.getProductsQuantity() );

        return memberProductResponseDto.build();
    }

    @Override
    public MemberProduct toEntity(MemberProductRequestDto requestDto, Member member, Product product) {
        if ( requestDto == null && member == null && product == null ) {
            return null;
        }

        MemberProductBuilder memberProduct = MemberProduct.builder();

        if ( requestDto != null ) {
            memberProduct.productsQuantity( requestDto.getProductsQuantity() );
        }
        if ( member != null ) {
            memberProduct.member( member );
        }
        if ( product != null ) {
            memberProduct.product( product );
        }

        return memberProduct.build();
    }

    private String memberProductProductTitle(MemberProduct memberProduct) {
        if ( memberProduct == null ) {
            return null;
        }
        Product product = memberProduct.getProduct();
        if ( product == null ) {
            return null;
        }
        String title = product.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private int memberProductProductPrice(MemberProduct memberProduct) {
        if ( memberProduct == null ) {
            return 0;
        }
        Product product = memberProduct.getProduct();
        if ( product == null ) {
            return 0;
        }
        int price = product.getPrice();
        return price;
    }
}
