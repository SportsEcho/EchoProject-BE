package com.sportsecho.hotdeal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SetUpHotdealRequestDto {

    @NotNull(message = "대기열 주문 가능 인원수를 입력해주세요")
    private int publishedSize;

}
