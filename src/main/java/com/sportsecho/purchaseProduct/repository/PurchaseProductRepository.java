package com.sportsecho.purchaseProduct.repository;

import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {

    @Modifying
    @Query("DELETE FROM PurchaseProduct pp WHERE pp.purchase.id = :purchaseId")
    void deleteByPurchaseId(@Param("purchaseId") Long purchaseId);
}
