package com.sportsecho.purchaseProduct.mapper;

import com.sportsecho.product.entity.Product;
import com.sportsecho.purchaseProduct.dto.PurchaseProductResponseDto;
import com.sportsecho.purchaseProduct.dto.PurchaseProductResponseDto.PurchaseProductResponseDtoBuilder;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-19T13:03:46+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class PurchaseProductMapperImpl implements PurchaseProductMapper {

    @Override
    public PurchaseProductResponseDto toResponseDto(PurchaseProduct purchaseProduct) {
        if ( purchaseProduct == null ) {
            return null;
        }

        PurchaseProductResponseDtoBuilder purchaseProductResponseDto = PurchaseProductResponseDto.builder();

        purchaseProductResponseDto.title( purchaseProductProductTitle( purchaseProduct ) );
        purchaseProductResponseDto.price( purchaseProductProductPrice( purchaseProduct ) );
        purchaseProductResponseDto.id( purchaseProduct.getId() );
        purchaseProductResponseDto.productsQuantity( purchaseProduct.getProductsQuantity() );

        return purchaseProductResponseDto.build();
    }

    private String purchaseProductProductTitle(PurchaseProduct purchaseProduct) {
        if ( purchaseProduct == null ) {
            return null;
        }
        Product product = purchaseProduct.getProduct();
        if ( product == null ) {
            return null;
        }
        String title = product.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private int purchaseProductProductPrice(PurchaseProduct purchaseProduct) {
        if ( purchaseProduct == null ) {
            return 0;
        }
        Product product = purchaseProduct.getProduct();
        if ( product == null ) {
            return 0;
        }
        int price = product.getPrice();
        return price;
    }
}
