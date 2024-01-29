package com.sportsecho.memberProduct.repository;

import com.sportsecho.memberProduct.entity.MemberProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProductRepository extends JpaRepository<MemberProduct, Long> {

    Optional<MemberProduct> findByProductIdAndMemberId(Long productId, Long memberId);

    @EntityGraph(value = "graph.MemberProduct", type = EntityGraph.EntityGraphType.FETCH)
    List<MemberProduct> findByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
