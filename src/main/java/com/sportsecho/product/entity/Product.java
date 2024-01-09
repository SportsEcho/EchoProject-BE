package com.sportsecho.product.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Product extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private int price;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Hotdeal hotdeal;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProduct> PurchaseProductList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberProduct> memberProductList = new ArrayList<>();

    public Product update(String title, String content, String imageUrl, int price) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.price = price;
        return this;
    }

    public void unlinkHotdeal() {
        this.hotdeal = null;
    }

    public void updateProductImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}