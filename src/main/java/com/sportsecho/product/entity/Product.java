package com.sportsecho.product.entity;

import com.sportsecho.cart.entity.Cart;
import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.product.dto.response.ProductResponseDto;
import com.sportsecho.puchase.entity.Purchase;
import com.sportsecho.puchaseProduct.entity.PurchaseProduct;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Hotdeal hotdeal;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchaseList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProduct> purchaseProductList = new ArrayList<>();

    @Builder
    public Product(String title, String content, String imageUrl, int price) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public static Product create(String title, String content, int price) {
        return Product.builder()
            .content(content)
            .content(content)
            .price(price)
            .build();
    }

    public Product update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        return this;
    }

    public void updateProductImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductResponseDto toProductResponseDto(Product product) {
        return new ProductResponseDto(
            product.getTitle(),
            product.getContent(),
            product.getImageUrl(),
            product.getPrice()
        );
    }
}
