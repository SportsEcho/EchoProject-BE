package com.sportsecho.hotdeal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHotdealResponseDto {

    private String title;
    private int price;
    private int remainQuantity;
    private String remainTime;

}
