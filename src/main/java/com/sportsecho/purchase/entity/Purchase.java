package com.sportsecho.purchase.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.member.entity.Member;
import com.sportsecho.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private Integer totalPrice;

    private String address;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Purchase(Integer totalPrice, String address, String phone) {
        this.totalPrice = totalPrice;
        this.address = address;
        this.phone = phone;
    }

    public Purchase create(Integer totalPrice, String address, String phone) {
        return Purchase.builder()
            .totalPrice(totalPrice)
            .address(address)
            .phone(phone)
            .build();
    }

    public void update(Integer totalPrice, String address, String phone) {
        this.address = address;
        this.phone = phone;
    }
}
