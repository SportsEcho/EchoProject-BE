package com.sportsecho.purchase.mapper;

import com.sportsecho.member.entity.Member;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.entity.Purchase;
import com.sportsecho.purchase.entity.Purchase.PurchaseBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-17T20:08:23+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class PurchaseMapperImpl implements PurchaseMapper {

    @Override
    public Purchase toEntity(PurchaseRequestDto requestDto, Member member) {
        if ( requestDto == null && member == null ) {
            return null;
        }

        PurchaseBuilder purchase = Purchase.builder();

        if ( requestDto != null ) {
            purchase.address( requestDto.getAddress() );
            purchase.phone( requestDto.getPhone() );
        }
        if ( member != null ) {
            purchase.member( member );
            purchase.id( member.getId() );
        }
        purchase.totalPrice( 0 );

        return purchase.build();
    }
}
