package com.sportsecho.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "상품명을 입력해주세요.")
    @Size(max = 30, message = "제목은 30자 이내로 입력해주세요.")
    private String title;
    @NotBlank(message = "상품 설명을 입력해주세요.")
    @Size(max = 100, message = "상품 설명은 100자 이내로 입력해주세요.")
    private String content;
    @NotBlank(message = "상품 가격을 입력해주세요.")
    private int price;
    @NotBlank(message = "상품 수량을 입력해주세요.")
    @Min(value = 0, message = "한정 수량은 0개 이상이어야 합니다.")
    private int quantity;

}
