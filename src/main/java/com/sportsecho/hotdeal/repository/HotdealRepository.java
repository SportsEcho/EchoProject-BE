package com.sportsecho.hotdeal.repository;

import com.sportsecho.hotdeal.entity.Hotdeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotdealRepository extends JpaRepository<Hotdeal, Long> {

    Page<Hotdeal> findAll(Pageable pageable);

}
