package com.sportsecho.hotdeal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseHotdealRequestDto {

    @NotNull(message = "hotdealId를 입력해주세요.")
    private Long hotdealId;
    @NotNull(message = "수량을 입력해주세요.")
    private int quantity;
    @NotBlank(message = "이름을 입력해주세요.")
    private String address;
    @NotBlank(message = "주소를 입력해주세요.")
    private String phone;

    private int threadNumber;
}
