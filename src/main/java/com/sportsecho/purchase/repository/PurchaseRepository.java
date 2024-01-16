package com.sportsecho.purchase.repository;

import com.sportsecho.purchase.entity.Purchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByMemberId(Long memberId);
}
