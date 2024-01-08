package com.sportsecho.purchase.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.mapper.MemberProductMapper;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
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

    @Builder
    public Purchase(int totalPrice, String address, String phone) {
        this.totalPrice = totalPrice;
        this.address = address;
        this.phone = phone;
    }

    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PurchaseResponseDto createResponseDto(List<MemberProduct> memberProductList) {
        return PurchaseResponseDto.builder()
            .totalPrice(this.totalPrice)
            .address(this.address)
            .phone(this.phone)
            .responseDtoList(
                memberProductList.stream()
                    .map(MemberProductMapper.INSTANCE::toResponseDto)
                    .toList()
            )
            .build();
    }
}
