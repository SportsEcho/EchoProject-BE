package com.sportsecho.purchase.dto;

import com.sportsecho.purchaseProduct.dto.PurchaseProductResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseListResponseDto {

    private LocalDateTime createdAt;

    private List<PurchaseProductResponseDto> responseDtoList;
}
