package com.sportsecho.hotdeal.repository;

import com.sportsecho.hotdeal.entity.Hotdeal;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface HotdealRepository extends JpaRepository<Hotdeal, Long> {

    Page<Hotdeal> findAll(Pageable pageable);

    List<Hotdeal> findAllByDueDayBefore(LocalDateTime now);

    List<Hotdeal> findAllByDealQuantity(int dealQuantity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from Hotdeal h where h.id = :id")
    Optional<Hotdeal> findByIdWithPessimisticWriteLock(@Param("id") Long id);

}
