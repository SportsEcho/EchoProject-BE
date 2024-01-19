package com.sportsecho.memberProduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberProductRequestDto {

    @NotNull(message = "수량은 필수 입력값입니다.")
    @Min(value = 1, message = "1개부터 구매 가능합니다.")
    private int productsQuantity;
}
