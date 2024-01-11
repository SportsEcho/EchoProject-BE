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
import java.util.SimpleTimeZone;
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

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceImplV1TestIntegration {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member member;

    @BeforeEach
    void setMember() {
        member = memberRepository.save(Member.builder()
            .memberName("testUser")
            .password("Password2@")
            .email("email@email.com")
            .role(MemberRole.ADMIN)
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

        private ProductRequestDto requestDto;

        @BeforeEach
        void setProductRequest() {
            requestDto = new ProductRequestDto("제목", "내용", "image.com", 3000, 10);
        }

        @Test
        @DisplayName("성공 - 상품 생성")
        void createProductSuccess() {

            // Given
            Member testMember = member;
            ProductRequestDto productRequestDto = requestDto;

            // When
            productService.createProduct(productRequestDto, testMember);

            // Then
            assertNotNull(productRepository);
        }

        @Test
        @DisplayName("실패 - 권한 없음(일반유저)")
        void createProductFail_NOAUTHORIZATION() {
            // Given
            Member customer = Member.builder()
                .memberName("customer")
                .password("Password2@")
                .email("customerEmail@email.com")
                .role(MemberRole.CUSTOMER)
                .build();

            // When & Then
            assertThrows(GlobalException.class, () -> {
                productService.createProduct(requestDto, customer);
            });
        }
    }

    @Nested
    @DisplayName("상품 조회 테스트")
    class GetProductTest {

        @Nested
        @DisplayName("단건 조회 테스트")
        class getSingleProductTest {

            private Product product;

            @BeforeEach
            void setProduct() {
                product = Product.builder()
                    .title("test 제목")
                    .content("test 내용")
                    .imageUrl("test.com")
                    .price(1900)
                    .quantity(100)
                    .build();
                productRepository.save(product);
            }

            @Test
            @DisplayName("성공 - 상품 단건 조회")
            void getProductSuccess() {
                // Given
                Long productId = product.getId();

                // When
                ProductResponseDto result = productService.getProduct(productId);

                // Then
                assertNotNull(result);
                assertEquals(result.getTitle(), product.getTitle(), "제목이 일치하지 않습니다.");
                assertEquals(result.getContent(), product.getContent(), "내용이 일치하지 않습니다.");
                assertEquals(result.getImageUrl(), product.getImageUrl(), "이미지 URL이 일치하지 않습니다.");
                assertEquals(result.getPrice(), product.getPrice(), "가격이 일치하지 않습니다.");
                assertEquals(result.getQuantity(), product.getQuantity(), "수량이 일치하지 않습니다.");
            }

            @Test
            @DisplayName("실패 - 존재하지 않는 상품")
            void getProductFail_NOTFOUNDPRODUCT() {
                // Given
                Long productId = 99L;


                // When && Then
                GlobalException thrown = assertThrows(GlobalException.class, () -> {
                    productService.getProduct(productId);
                }, "예상한 GlobalException이 발생하지 않았습니다.");
                assertEquals(ProductErrorCode.NOT_FOUND_PRODUCT, thrown.getErrorCode(), "예외의 에러 코드가 NOT_FOUND_PRODUCT가 아닙니다.");
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
                // Given
                Sort sort = Sort.by(Sort.Direction.fromString("asc"), "price");
                int pageSize = 5;
                Pageable pageable = PageRequest.of(0, pageSize, sort);
                List<Integer> allPrices = productRepository.findAll(Sort.by("price")).stream()
                    .map(Product::getPrice)
                    .toList(); // 상품 가격을 오름차순으로 정렬해서 가져옴

                // When
                List<ProductResponseDto> productList = productService.getProductListWithPagiNation(
                    pageable);

                // Then
                assertEquals(pageSize, productList.size(), "페이지 수가 일치하지 않습니다");
                assertEquals(productList.get(0).getPrice(), allPrices.get(0), "정렬 결과가 올바르지 않습니다"); // 테스트 결과의 첫 번째 상품의 가격이 전체 상품 가격 중 가장 낮은지 확인
            }

        }

    }

    @Nested
    @DisplayName("상품 수정 테스트")
    class updateProductTest {

        private Product product;
        private ProductRequestDto requestDto;

        @BeforeEach
        void setProductRequest() {
            product = Product.builder()
                .title("test 제목")
                .content("test 내용")
                .imageUrl("test.com")
                .price(1900)
                .quantity(100)
                .build();
            productRepository.save(product);

            requestDto = new ProductRequestDto("수정 제목", "수정 내용", "newImage.com", 999, 99);


        }

        @Test
        @DisplayName("성공 - 상품 수정")
        void updateProductSuccess() {
            // Given
            String newTitle = requestDto.getTitle();
            String newContent = requestDto.getContent();
            String newImageUrl = requestDto.getImageUrl();
            int newPrice = requestDto.getPrice();
            int newQuatntity = requestDto.getQuantity();

            // When
            ProductResponseDto responseDto = productService.updateProduct(member, product.getId(), requestDto);

            // Then
            assertEquals(newTitle, responseDto.getTitle(), "제목이 일치하지 않습니다.");
            assertEquals(newContent, responseDto.getContent(), "내용이 일치하지 않습니다.");
            assertEquals(newImageUrl, responseDto.getImageUrl(), "이미지 URL이 일치하지 않습니다.");
            assertEquals(newPrice, responseDto.getPrice(), "가격이 일치하지 않습니다.");
            assertEquals(newQuatntity, responseDto.getQuantity(), "수량이 일치하지 않습니다.");
        }

        @Test
        @DisplayName("실패 - 권한 없음(일반유저)")
        void updateProductFail_NOAUTHORIZATION() {
            // Given
            Member customer = Member.builder()
                .memberName("customer")
                .password("Password2@")
                .email("customerEmail@email.com")
                .role(MemberRole.CUSTOMER)
                .build();

            // When & Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                productService.updateProduct(customer, product.getId(), requestDto);
            }, "예상한 GlobalException이 발생하지 않았습니다.");
            assertEquals(ProductErrorCode.NO_AUTHORIZATION, thrown.getErrorCode(), "예외의 에러 코드가 NO_AUTHORIZATION이 아닙니다.");
        }

    }

    @Nested
    @DisplayName("상품 삭제 테스트")
    class deleteProduct {
        private Product product;

        @BeforeEach
        void setProduct() {
            product = Product.builder()
                .title("test 제목")
                .content("test 내용")
                .imageUrl("test.com")
                .price(1900)
                .quantity(100)
                .build();
            productRepository.save(product);
        }

        @Test
        @DisplayName("성공 - 상품 삭제")
        void deleteProductSuccess() {
            // Given
            Long productId = product.getId();

            // When
            productService.deleteProduct(member, productId);

            // Then
            assertTrue(productRepository.findById(productId).isEmpty());
        }

        @Test
        @DisplayName("실패 - 상품 삭제")
        void deleteProductFail_NOTFOUNDPRODUCT() {
            // Given
            Long productId = 99L;

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                productService.deleteProduct(member, productId);
            }, "예상한 GlobalException이 발생하지 않았습니다.");
            assertEquals(ProductErrorCode.NOT_FOUND_PRODUCT, thrown.getErrorCode(), "예외의 에러 코드가 NOT_FOUND_PRODUCT가 아닙니다.");
        }
    }

}