package com.sportsecho.product.repository;

import com.sportsecho.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByTitleContaining(Pageable pageable, String keyword);

}