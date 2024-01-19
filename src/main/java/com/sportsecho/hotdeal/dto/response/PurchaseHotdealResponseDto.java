package com.sportsecho.hotdeal.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class PurchaseHotdealResponseDto {

    private String title;
    private int price;
    private int remainQuantity;
    private String remainTime;

}
