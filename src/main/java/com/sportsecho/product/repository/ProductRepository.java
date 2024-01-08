package com.sportsecho.product.repository;

import com.sportsecho.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // QueryDsl, JPQL을 사용할때 모두 비교하고 벤치마킹
    Page<Product> findAllWithPagination(Pageable pageable);
  
}
