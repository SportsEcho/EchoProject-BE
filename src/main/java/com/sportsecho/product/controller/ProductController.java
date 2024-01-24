package com.sportsecho.product.controller;

import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.dto.response.ProductResponseDto;
import com.sportsecho.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(@Qualifier("V1") ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<ProductResponseDto> createProduct(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @Valid @RequestBody ProductRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            productService.createProduct(requestDto, memberDetails.getMember())
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(
        @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            productService.getProduct(productId)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponseDto>> getProductListWithPageNation(
        Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            productService.getProductListWithPagiNation(pageable)
        );
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long productId,
        @Valid @RequestBody ProductRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            productService.updateProduct(memberDetails.getMember(), productId, requestDto)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long productId
    ) {
        productService.deleteProduct(memberDetails.getMember(), productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}


