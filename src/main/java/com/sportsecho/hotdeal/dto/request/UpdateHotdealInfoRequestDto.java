package com.sportsecho.hotdeal.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateHotdealInfoRequestDto {

    private LocalDateTime startDay;
    private LocalDateTime dueDay;
    private int dealQuantity;
    private int sale;

}
