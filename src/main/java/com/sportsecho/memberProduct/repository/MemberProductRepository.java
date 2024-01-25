package com.sportsecho.memberProduct.repository;

import com.sportsecho.memberProduct.entity.MemberProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberProductRepository extends JpaRepository<MemberProduct, Long> {

    Optional<MemberProduct> findByProductIdAndMemberId(Long productId, Long memberId);

    @Query("SELECT mp FROM MemberProduct mp JOIN FETCH mp.product p JOIN FETCH p.productImageList WHERE mp.member.id = :memberId")
    List<MemberProduct> findByMemberId(@Param("memberId") Long memberId);

    void deleteAllByMemberId(Long memberId);
}
