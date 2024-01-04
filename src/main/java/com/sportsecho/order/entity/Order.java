package com.sportsecho.order.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.member.entity.Member;
import com.sportsecho.product.entity.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Order extends TimeStamp {

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
    public Order(Integer totalPrice, String address, String phone){
        this.totalPrice = totalPrice;
        this.address = address;
        this.phone = phone;
    }

    public Order create(Integer totalPrice, String address, String phone) {
        return Order.builder()
                .totalPrice(totalPrice)
                .address(address)
                .phone(phone)
                .build();
    }

    public void update(Integer totalPrice, String address, String phone){
        this.address = address;
        this.phone = phone;
    }
}
