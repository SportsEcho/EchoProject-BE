package com.sportsecho.product.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.dto.response.ProductResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto requestDto, Member member);

    ProductResponseDto getProduct(Long productId);

    List<ProductResponseDto> getProductListWithPagiNation(Pageable pageable);

    ProductResponseDto updateProduct(Member member, Long productId, ProductRequestDto requestDto);

    void deleteProduct(Member member, Long productId);

}
