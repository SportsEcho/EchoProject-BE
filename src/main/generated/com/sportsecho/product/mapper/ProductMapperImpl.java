package com.sportsecho.product.mapper;

import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.dto.response.ProductResponseDto;
import com.sportsecho.product.dto.response.ProductResponseDto.ProductResponseDtoBuilder;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.entity.Product.ProductBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-19T13:36:19+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponseDto toResponseDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponseDtoBuilder productResponseDto = ProductResponseDto.builder();

        productResponseDto.title( product.getTitle() );
        productResponseDto.content( product.getContent() );
        productResponseDto.imageUrl( product.getImageUrl() );
        productResponseDto.price( product.getPrice() );
        productResponseDto.quantity( product.getQuantity() );

        return productResponseDto.build();
    }

    @Override
    public Product toEntity(ProductRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        ProductBuilder product = Product.builder();

        product.title( requestDto.getTitle() );
        product.content( requestDto.getContent() );
        product.imageUrl( requestDto.getImageUrl() );
        product.price( requestDto.getPrice() );
        product.quantity( requestDto.getQuantity() );

        return product.build();
    }
}
