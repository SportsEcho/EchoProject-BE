package com.sportsecho.purchase.dto;

import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
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

    private int totalPrice;

    private String address;

    private String phone;

    private List<MemberProductResponseDto> responseDtoList;
}
