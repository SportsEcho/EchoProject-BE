package com.sportsecho.product.repository;

import com.sportsecho.product.entity.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByTitleContaining(Pageable pageable, String keyword);

    @Query("SELECT p FROM Product p LEFT JOIN fetch p.productImageList WHERE p.id = :productId")
    Optional<Product> findByIdWithImages(Long productId);
}