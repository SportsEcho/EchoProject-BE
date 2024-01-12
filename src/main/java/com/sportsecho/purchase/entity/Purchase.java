package com.sportsecho.purchase.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.member.entity.Member;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import com.sportsecho.purchaseProduct.mapper.PurchaseProductMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Purchase extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalPrice;

    private String address;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProduct> purchaseProductList = new ArrayList<>();

    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    // 연관 관계가 복잡하여 mapper를 사용하지 않음.. 추후 수정 가능성 있음..
    public PurchaseResponseDto createResponseDto() {
        return PurchaseResponseDto.builder()
                .totalPrice(this.totalPrice)
                .address(this.address)
                .phone(this.phone)
                .purchaseDate(this.getCreatedAt())
                .responseDtoList(
                        this.purchaseProductList.stream()
                                .map(PurchaseProductMapper.INSTANCE::toResponseDto)
                                .toList()
                )
                .build();
    }
}
