package com.sportsecho.product.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

//    @Version
    //@Version 애너테이션의 정확한 동작 방식을 알아보기
    //관련 없는 쿼리를 날렸을때 어떤 영향이 발생하는지
    private Long version;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PurchaseProduct> PurchaseProductList = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MemberProduct> memberProductList = new ArrayList<>();

    @BatchSize(size = 32)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductImage> productImageList = new ArrayList<>();

    @Builder
    public Product(String title, String content, int price, int quantity) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
    }

    public Product update(String title, String content, int price, int quantity) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        return this;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}