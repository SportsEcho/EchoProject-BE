package com.sportsecho.product.controller;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.dto.response.ProductResponseDto;
import com.sportsecho.product.service.ProductService;
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
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private @Qualifier("V1") ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @RequestBody ProductRequestDto requestDto
    ) {

        ProductResponseDto responseDto = productService.createProduct(requestDto,
            memberDetails.getMember());


        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.of("상품 생성 성공", 201, responseDto)
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProduct(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long productId
    ) {

        ProductResponseDto responseDto = productService.getProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("상품 단건 조회 성공", 200, responseDto)
        );
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductListWithPageNation(
        Pageable pageable
    ) {
        List<ProductResponseDto> responseDtoList = productService.getProductListWithPagiNation(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("상품 목록 조회 성공", 200, responseDtoList)
        );
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long productId,
        @RequestBody ProductRequestDto requestDto
    )  {

        ProductResponseDto responseDto = productService.updateProduct(memberDetails.getMember(), productId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("상품 수정 성공", 200, responseDto)
        );
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long productId
    ) {
        productService.deleteProduct(memberDetails.getMember(), productId);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("상품 삭제 성공", 204, null)
        );
    }

}


