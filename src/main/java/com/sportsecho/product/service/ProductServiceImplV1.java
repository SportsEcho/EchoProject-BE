package com.sportsecho.product.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.dto.response.ProductResponseDto;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.mapper.ProductMapper;
import com.sportsecho.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Qualifier("V1")
@Service
@RequiredArgsConstructor
public class ProductServiceImplV1 implements ProductService{

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, Member member) {
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
    public List<ProductResponseDto> getProductListWithPagiNation(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.stream()
            .map(ProductMapper.INSTANCE::toResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Member member, Long productId, ProductRequestDto requestDto) {
        Product product = findProduct(productId);
        product.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl(), requestDto.getPrice());
        productRepository.save(product);

        return ProductMapper.INSTANCE.toResponseDto(product);
    }

    @Override
    public void deleteProduct(Member member, Long productId) {
        Product product = findProduct(productId);

        if (member.getRole().equals(MemberRole.CUSTOMER)) {
            throw new RuntimeException();
        }

        productRepository.delete(product);
    }

    private Product findProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new RuntimeException();
        }

        return product.get();
    }
}
