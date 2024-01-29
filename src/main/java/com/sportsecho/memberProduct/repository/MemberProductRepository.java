package com.sportsecho.memberProduct.repository;

import com.sportsecho.memberProduct.entity.MemberProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberProductRepository extends JpaRepository<MemberProduct, Long> {

    Optional<MemberProduct> findByProductIdAndMemberId(Long productId, Long memberId);

    @EntityGraph(value = "graph.MemberProduct", type = EntityGraph.EntityGraphType.FETCH)
    List<MemberProduct> findByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM MemberProduct mp WHERE mp.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
