package com.sportsecho.memberProduct.repository;

import com.sportsecho.memberProduct.entity.MemberProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProductRepository extends JpaRepository<MemberProduct, Long> {

    Optional<MemberProduct> findByProductIdAndMemberId(Long productId, Long memberId);

    List<MemberProduct> findByMemberId(Long id);

    void deleteByMemberId(Long memberId);
}
