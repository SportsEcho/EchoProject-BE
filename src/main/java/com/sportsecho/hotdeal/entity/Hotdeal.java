package com.sportsecho.hotdeal.entity;

import com.sportsecho.product.entity.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Hotdeal{

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
    public Hotdeal(LocalDateTime startDay, LocalDateTime dueDay, int dealQuantity, int sale) {
        this.startDay = startDay;
        this.dueDay = dueDay;
        this. dealQuantity = dealQuantity;
        this.sale = sale;
    }

    public Hotdeal create(LocalDateTime startDay, LocalDateTime dueDay, int dealQuantity, int sale) {
        return Hotdeal.builder()
                .startDay(startDay)
                .dueDay(dueDay)
                .dealQuantity(dealQuantity)
                .sale(sale)
                .build();
    }

    public void updateDealQuantity(int dealQuantity) {
        this.dealQuantity = dealQuantity;
    }

    public void updateSale(int sale) {
        this.sale = sale;
    }
}
