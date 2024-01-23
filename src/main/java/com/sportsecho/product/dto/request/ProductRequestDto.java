package com.sportsecho.product.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequestDto {

    private String title;
    private String content;
    private int price;
    private int quantity;

}
