package com.sportsecho.memberProduct.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class MemberProductServiceImplV1IntTest {

    @Autowired
    MemberProductRepository memberProductRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberProductServiceImplV1 memberProductService;

    Member member;
    Product product;
    MemberProductRequestDto requestDto;

    @BeforeEach
    void setUp() {
        member = new Member("name", "member@email.com", "pass", MemberRole.CUSTOMER);
        product = new Product("상품", "상품", "image", 10000, 100);
        requestDto = new MemberProductRequestDto();

        ReflectionTestUtils.setField(requestDto, "productsQuantity", 3);

        memberRepository.save(member);
        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        memberProductRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Nested
    @DisplayName("장바구니에 상품 추가 test")
    class addCartTest {
        @Test
        @DisplayName("장바구니 추가 성공 - 새 상품")
        void addCartTest_success_new() {
            //when
            MemberProductResponseDto responseDto = memberProductService.addCart(product.getId(), requestDto, member);

            //then
            assertNotNull(responseDto);
            assertEquals(requestDto.getProductsQuantity(), responseDto.getProductsQuantity());
            assertEquals(product.getPrice(), responseDto.getPrice());
            assertEquals(product.getTitle(), responseDto.getTitle());
        }
    }
}