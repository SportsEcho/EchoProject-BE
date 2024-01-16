package com.sportsecho.purchaseProduct.repository;

import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {

}
