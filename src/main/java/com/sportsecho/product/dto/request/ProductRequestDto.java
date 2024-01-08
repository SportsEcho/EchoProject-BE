package com.sportsecho.product.dto.request;

import lombok.Getter;

@Getter
public class ProductRequestDto {

    private String title;
    private String content;
    private String imageUrl;
    private int price;

}
