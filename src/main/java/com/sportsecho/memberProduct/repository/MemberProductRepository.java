package com.sportsecho.memberProduct.repository;

import com.sportsecho.memberProduct.entity.MemberProduct;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MemberProductRepository extends JpaRepository<MemberProduct, Long> {

    Optional<MemberProduct> findByProductIdAndMemberId(Long productId, Long memberId);


    @Lock(LockModeType.OPTIMISTIC)
    @Query("select mp from MemberProduct mp join fetch mp.product p where mp.member.id = :memberId")
    List<MemberProduct> findByMemberIdJoinProductWithOptLock(@Param("memberId") Long memberId);

    List<MemberProduct> findByMemberId(Long id);

    void deleteAllByMemberId(Long memberId);
}
