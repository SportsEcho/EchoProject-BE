package com.sportsecho.hotdeal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HotdealRequestDto {

    @NotBlank(message = "시작일을 입력해주세요.")
    private LocalDateTime startDay;
    @NotBlank(message = "종료일을 입력해주세요.")
    private LocalDateTime dueDay;
    @NotNull(message = "수량을 입력해주세요.")
    private int dealQuantity;
    @NotNull(message = "할인율을 입력해주세요.")
    private int sale;

}
