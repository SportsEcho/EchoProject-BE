package com.sportsecho.hotdeal.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseHotdealRequestDto {

    private Long hotdealId;
    private int quantity;

}
