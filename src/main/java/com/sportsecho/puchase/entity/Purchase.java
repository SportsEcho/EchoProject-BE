package com.sportsecho.puchase.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.member.entity.Member;
import com.sportsecho.product.entity.Product;
import com.sportsecho.puchaseProduct.entity.PurchaseProduct;
import jakarta.persistence.*;
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

    private Integer totalPrice;

    private String address;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProduct> purchaseProductList = new ArrayList<>();

    @Builder
    public Purchase(Integer totalPrice, String address, String phone){
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

    public void update(Integer totalPrice, String address, String phone){
        this.address = address;
        this.phone = phone;
    }
}
