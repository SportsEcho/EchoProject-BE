package com.sportsecho.purchase.repository;

import com.sportsecho.purchase.entity.Purchase;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @EntityGraph(value = "graph.Purchase", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT p FROM Purchase p WHERE p.member.id = :memberId ORDER BY p.createdAt DESC")
    List<Purchase> findByMemberId(@Param("memberId") Long memberId);
}
