package com.sportsecho.hotdeal.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotdealResponseDto {

    private Long id;
    private String title;
    private String content;
    private int price;
    private int sale;
    private int dealQuantity;
    private LocalDateTime startDay;
    private LocalDateTime dueDay;
    private List<String> imageUrlList;

}
