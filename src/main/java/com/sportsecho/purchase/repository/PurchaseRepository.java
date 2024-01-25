package com.sportsecho.purchase.repository;

import com.sportsecho.purchase.entity.Purchase;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p JOIN FETCH p.purchaseProductList pp JOIN FETCH pp.product WHERE p.member.id = :memberId ORDER BY p.createdAt DESC")
    List<Purchase> findByMemberId(@Param("memberId") Long memberId);
}
