package com.sportsecho.hotdeal.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HotdealResponseDto {

    private String title;
    private String content;
    private String imageUrl;
    private int price;
    private int sale;
    private LocalDateTime startDay;
    private LocalDateTime dueDay;

}
