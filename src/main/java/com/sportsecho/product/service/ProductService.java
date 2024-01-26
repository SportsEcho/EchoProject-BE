package com.sportsecho.product.service;

import com.sportsecho.product.dto.ProductRequestDto;
import com.sportsecho.product.dto.ProductResponseDto;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ProductService {

    /**
     * Product 생성 - ADMIN 권한이 있는 사용자만 생성 가능
     * @param requestDto Product 생성 요청 정보
     * @return 생성된 Product 정보 반환
     * */
    ProductResponseDto createProduct(ProductRequestDto requestDto);

    /**
     * Product 단건 조회
     * @param productId 단건 조회할 Product의 ID
     * @return 단건 Product 정보 반환
     * */
    ProductResponseDto getProduct(Long productId);

    /**
     * Product 페이지 조회
     * @param pageable 페이지 page, size 정보를 담은 Pegeable 객체
     * @return 페이지 Product 정보 반환
     * */
    List<ProductResponseDto> getProductListWithPageNation(Pageable pageable, String keyword);

    /**
     * Product 수정 - ADMIN 권한이 있는 사용자만 수정 가능
     * @param productId 수정할 Product의 ID
     * @param requestDto Product 수정 요청 정보
     * */
    ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto);

    /**
     * Product 삭제 - ADMIN 권한이 있는 사용자만 삭제 가능
     * @param productId 삭제할 Product의 ID
     * */
    void deleteProduct(Long productId);

}
