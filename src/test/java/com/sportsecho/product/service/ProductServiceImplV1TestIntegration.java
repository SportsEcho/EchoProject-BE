package com.sportsecho.product.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.dto.response.ProductResponseDto;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceImplV1TestIntegration {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member adminMember, customerMember;
    private Product product;
    private ProductRequestDto requestDto = new ProductRequestDto();

    @BeforeEach
    void setUp() {
        adminMember = createMember("admin", MemberRole.ADMIN);
        customerMember = createMember("customer", MemberRole.CUSTOMER);
        product = productRepository.save(Product.builder()
            .title("test 제목")
            .content("test 내용")
            .imageUrl("test.com")
            .price(1900)
            .quantity(100)
            .build());

        ReflectionTestUtils.setField(requestDto, "title", "제목", String.class);
        ReflectionTestUtils.setField(requestDto, "content", "내용", String.class);
        ReflectionTestUtils.setField(requestDto, "imageUrl", "image.com", String.class);
        ReflectionTestUtils.setField(requestDto, "price", 3000, int.class);
        ReflectionTestUtils.setField(requestDto, "quantity", 10, int.class);
    }

    private Member createMember(String memberName, MemberRole role) {
        return memberRepository.save(Member.builder()
            .memberName(memberName)
            .password("Password2@")
            .email(memberName + "@email.com")
            .role(role)
            .build());
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Nested
    @DisplayName("상품 생성 테스트")
    class ProductSaveTest {

        @Test
        @DisplayName("성공 - 상품 생성")
        void createProductSuccess() {
            productService.createProduct(requestDto, adminMember);
            assertNotNull(productRepository);
        }

        @Test
        @DisplayName("실패 - 권한 없음(일반유저)")
        void createProductFail_NOAUTHORIZATION() {
            assertThrows(GlobalException.class, () -> {
                productService.createProduct(requestDto, customerMember);
            });
        }
    }

    @Nested
    @DisplayName("상품 조회 테스트")
    class GetProductTest {

        @Nested
        @DisplayName("단건 조회 테스트")
        class getSingleProductTest {

            @Test
            @DisplayName("성공 - 상품 단건 조회")
            void getProductSuccess() {
                ProductResponseDto result = productService.getProduct(product.getId());
                assertNotNull(result);
                assertEquals(result.getTitle(), product.getTitle());
                assertEquals(result.getContent(), product.getContent());
                assertEquals(result.getImageUrl(), product.getImageUrl());
                assertEquals(result.getPrice(), product.getPrice());
                assertEquals(result.getQuantity(), product.getQuantity());
            }

            @Test
            @DisplayName("실패 - 존재하지 않는 상품")
            void getProductFail_NOTFOUNDPRODUCT() {
                Long productId = 99L;
                GlobalException thrown = assertThrows(GlobalException.class, () -> {
                    productService.getProduct(productId);
                });
                assertEquals(ProductErrorCode.NOT_FOUND_PRODUCT, thrown.getErrorCode());
            }
        }

        @Nested
        @DisplayName("목록 조회 테스트")
        class getProductListTest {

            @BeforeEach
            void setProductList() {
                for (int i = 0; i < 10; i++) {
                    productRepository.save(Product.builder()
                        .title(i + "번 상품 제목")
                        .content(i + "번 상품내용")
                        .imageUrl("image.com")
                        .price(i * 1000)
                        .quantity(i * 100)
                        .build());
                }
            }

            @Test
            @DisplayName("성공 - 상품 목록 조회(가격기준 오름차순)")
            void getProductListSuccess() {
                Sort sort = Sort.by(Sort.Direction.fromString("asc"), "price");
                int pageSize = 5;
                Pageable pageable = PageRequest.of(0, pageSize, sort);
                List<Integer> allPrices = productRepository.findAll(Sort.by("price")).stream()
                    .map(Product::getPrice)
                    .toList();

                List<ProductResponseDto> productList = productService.getProductListWithPagiNation(pageable);
                assertEquals(pageSize, productList.size());
                assertEquals(productList.get(0).getPrice(), allPrices.get(0));
            }
        }
    }

    @Nested
    @DisplayName("상품 수정 테스트")
    class updateProductTest {

        @Test
        @DisplayName("성공 - 상품 수정")
        void updateProductSuccess() {
            ProductResponseDto responseDto = productService.updateProduct(adminMember, product.getId(), requestDto);
            assertEquals(requestDto.getTitle(), responseDto.getTitle());
            assertEquals(requestDto.getContent(), responseDto.getContent());
            assertEquals(requestDto.getImageUrl(), responseDto.getImageUrl());
            assertEquals(requestDto.getPrice(), responseDto.getPrice());
            assertEquals(requestDto.getQuantity(), responseDto.getQuantity());
        }

        @Test
        @DisplayName("실패 - 권한 없음(일반유저)")
        void updateProductFail_NOAUTHORIZATION() {
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                productService.updateProduct(customerMember, product.getId(), requestDto);
            });
            assertEquals(ProductErrorCode.NO_AUTHORIZATION, thrown.getErrorCode());
        }
    }

    @Nested
    @DisplayName("상품 삭제 테스트")
    class deleteProductTest {

        @Test
        @DisplayName("성공 - 상품 삭제")
        void deleteProductSuccess() {
            productService.deleteProduct(adminMember, product.getId());
            assertTrue(productRepository.findById(product.getId()).isEmpty());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품")
        void deleteProductFail_NOTFOUNDPRODUCT() {
            Long productId = 99L;
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                productService.deleteProduct(adminMember, productId);
            });
            assertEquals(ProductErrorCode.NOT_FOUND_PRODUCT, thrown.getErrorCode());
        }
    }
}