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
public class PurchaseResponseDto {

    private Long id;

    private int totalPrice;

    private String address;

    private String phone;

    private LocalDateTime purchaseDate;

    private List<PurchaseProductResponseDto> purchaseProductList;
}
