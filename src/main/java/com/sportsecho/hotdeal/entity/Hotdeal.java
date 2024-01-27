package com.sportsecho.hotdeal.entity;

import com.sportsecho.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "hotdeal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hotdeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startDay", nullable = false)
    private LocalDateTime startDay;

    @Column(name = "dueDay", nullable = false)
    private LocalDateTime dueDay;

    @Column(name = "dealQuantity", nullable = false)
    private int dealQuantity;

    @Column(name = "sale", nullable = false)
    private int sale;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Builder
    public Hotdeal(LocalDateTime startDay, LocalDateTime dueDay, int dealQuantity, int sale,
        Product product) {
        this.startDay = startDay;
        this.dueDay = dueDay;
        this.dealQuantity = dealQuantity;
        this.sale = sale;
        this.product = product;
    }

    public static Hotdeal of(LocalDateTime startDay, LocalDateTime dueDay, int dealQuantity,
        int sale, Product product) {
        return Hotdeal.builder()
            .startDay(startDay)
            .dueDay(dueDay)
            .dealQuantity(dealQuantity)
            .sale(sale)
            .product(product)
            .build();
    }

    public void updateDealQuantity(int dealQuantity) {
        this.dealQuantity = dealQuantity;
    }

    public void updateHotdealInfo(LocalDateTime startDay, LocalDateTime dueDay, int sale,
        int dealQuantity) {
        this.startDay = startDay;
        this.dueDay = dueDay;
        this.sale = sale;
        this.dealQuantity = dealQuantity;
    }
}
