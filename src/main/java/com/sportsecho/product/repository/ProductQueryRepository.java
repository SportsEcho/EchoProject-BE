package com.sportsecho.product.repository;

import com.sportsecho.product.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductQueryRepository {

    /**
     * Product 페이지단위 검색
     * @param pageable 클라이언트로부터 전달받은 page, size, sort 정보를 담은 객체
     * @param keyword 검색 키워드
     * @return 조건에 맞는 Product 페이지단위 검색 리스트 반환
     * */
    List<Product> findAllByKeywordWithPage(Pageable pageable, String keyword);
}
