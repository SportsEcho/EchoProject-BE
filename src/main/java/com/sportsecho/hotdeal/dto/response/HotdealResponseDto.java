package com.sportsecho.hotdeal.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HotdealResponseDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private int price;
    private int sale;
    private int dealQuantity;
    private LocalDateTime startDay;
    private LocalDateTime dueDay;

}
