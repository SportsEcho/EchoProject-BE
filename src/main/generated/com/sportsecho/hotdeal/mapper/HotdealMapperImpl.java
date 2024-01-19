package com.sportsecho.hotdeal.mapper;

import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto.HotdealResponseDtoBuilder;
import com.sportsecho.hotdeal.dto.response.PurchaseHotdealResponseDto;
import com.sportsecho.hotdeal.dto.response.PurchaseHotdealResponseDto.PurchaseHotdealResponseDtoBuilder;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.entity.Hotdeal.HotdealBuilder;
import com.sportsecho.product.entity.Product;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-19T13:36:19+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class HotdealMapperImpl implements HotdealMapper {

    @Override
    public Hotdeal toEntity(HotdealRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        HotdealBuilder hotdeal = Hotdeal.builder();

        hotdeal.startDay( requestDto.getStartDay() );
        hotdeal.dueDay( requestDto.getDueDay() );
        hotdeal.dealQuantity( requestDto.getDealQuantity() );
        hotdeal.sale( requestDto.getSale() );

        return hotdeal.build();
    }

    @Override
    public HotdealRequestDto toRequestDto(Hotdeal entity) {
        if ( entity == null ) {
            return null;
        }

        HotdealRequestDto hotdealRequestDto = new HotdealRequestDto();

        return hotdealRequestDto;
    }

    @Override
    public HotdealResponseDto toResponseDto(Hotdeal entity) {
        if ( entity == null ) {
            return null;
        }

        HotdealResponseDtoBuilder hotdealResponseDto = HotdealResponseDto.builder();

        hotdealResponseDto.title( entityProductTitle( entity ) );
        hotdealResponseDto.content( entityProductContent( entity ) );
        hotdealResponseDto.imageUrl( entityProductImageUrl( entity ) );
        hotdealResponseDto.price( entityProductPrice( entity ) );
        hotdealResponseDto.sale( entity.getSale() );
        hotdealResponseDto.startDay( entity.getStartDay() );
        hotdealResponseDto.dueDay( entity.getDueDay() );
        hotdealResponseDto.dealQuantity( entity.getDealQuantity() );

        return hotdealResponseDto.build();
    }

    @Override
    public PurchaseHotdealResponseDto toPurchaseResponseDto(Hotdeal hotdeal) {
        if ( hotdeal == null ) {
            return null;
        }

        PurchaseHotdealResponseDtoBuilder purchaseHotdealResponseDto = PurchaseHotdealResponseDto.builder();

        purchaseHotdealResponseDto.title( entityProductTitle( hotdeal ) );

        afterMappingToPurchaseResponseDto( hotdeal, purchaseHotdealResponseDto );

        return purchaseHotdealResponseDto.build();
    }

    private String entityProductTitle(Hotdeal hotdeal) {
        if ( hotdeal == null ) {
            return null;
        }
        Product product = hotdeal.getProduct();
        if ( product == null ) {
            return null;
        }
        String title = product.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private String entityProductContent(Hotdeal hotdeal) {
        if ( hotdeal == null ) {
            return null;
        }
        Product product = hotdeal.getProduct();
        if ( product == null ) {
            return null;
        }
        String content = product.getContent();
        if ( content == null ) {
            return null;
        }
        return content;
    }

    private String entityProductImageUrl(Hotdeal hotdeal) {
        if ( hotdeal == null ) {
            return null;
        }
        Product product = hotdeal.getProduct();
        if ( product == null ) {
            return null;
        }
        String imageUrl = product.getImageUrl();
        if ( imageUrl == null ) {
            return null;
        }
        return imageUrl;
    }

    private int entityProductPrice(Hotdeal hotdeal) {
        if ( hotdeal == null ) {
            return 0;
        }
        Product product = hotdeal.getProduct();
        if ( product == null ) {
            return 0;
        }
        int price = product.getPrice();
        return price;
    }
}
