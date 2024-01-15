package com.sportsecho.purchaseProduct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProductResponseDto {

    private int productsQuantity;

    private String title;

    private int price;
}
