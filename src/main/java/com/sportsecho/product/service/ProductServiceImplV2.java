package com.sportsecho.product.service;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.product.dto.ProductRequestDto;
import com.sportsecho.product.dto.ProductResponseDto;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.mapper.ProductMapper;
import com.sportsecho.product.repository.ProductQueryRepository;
import com.sportsecho.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("V2")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImplV2 implements ProductService {

    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = ProductMapper.INSTANCE.toEntity(requestDto);
        productRepository.save(product);

        return ProductMapper.INSTANCE.toResponseDto(product);
    }

    @Override
    public ProductResponseDto getProduct(Long productId) {
        Product product = findProduct(productId);

        return ProductMapper.INSTANCE.toResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> getProductListWithPageNation(Pageable pageable, String keyword) {
        List<Product> productPage = productQueryRepository.findAllByKeywordWithPage(pageable, keyword);

        return productPage.stream()
                .map(ProductMapper.INSTANCE::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto) {
        Product product = findProduct(productId);

        product.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getPrice(),
                requestDto.getQuantity());

        productRepository.save(product);

        return ProductMapper.INSTANCE.toResponseDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = findProduct(productId);

        productRepository.delete(product);
    }

    private Product findProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new GlobalException(ProductErrorCode.NOT_FOUND_PRODUCT);
        }

        return product.get();
    }
}
