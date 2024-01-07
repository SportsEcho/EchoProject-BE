package com.sportsecho.memberProduct.dto;

import com.sportsecho.memberProduct.entity.MemberProduct;
import lombok.Getter;

@Getter
public class MemberProductResponseDto {

    private int productsQuantity;

    private String title;

    private int price;

    public MemberProductResponseDto(MemberProduct memberProduct) {
        this.productsQuantity = memberProduct.getProductsQuantity();
        this.title = memberProduct.getProduct().getTitle();
        this.price = memberProduct.getProduct().getPrice();
    }
}
