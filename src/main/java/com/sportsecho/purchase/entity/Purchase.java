package com.sportsecho.purchase.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.member.entity.Member;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "graph.Purchase",
    attributeNodes = {
        @NamedAttributeNode(value = "purchaseProductList", subgraph = "purchaseProductListGraph")
    },
    subgraphs = {
        @NamedSubgraph(name = "purchaseProductListGraph", attributeNodes = {
            @NamedAttributeNode(value = "product")})
    })
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

    @Builder.Default
    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PurchaseProduct> purchaseProductList = new ArrayList<>();

    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
