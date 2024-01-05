package com.sportsecho.cart.entity;

import com.sportsecho.member.entity.Member;
import com.sportsecho.product.entity.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

    private Integer productsQuantity;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Cart(Integer productsQuantity){
        this.productsQuantity = productsQuantity;
    }

    public Cart create(Integer productsQuantity){
        return Cart.builder()
                .productsQuantity(productsQuantity)
                .build();
    }

    public void update(Integer productsQuantity){
        this.productsQuantity = productsQuantity;
    }
}
