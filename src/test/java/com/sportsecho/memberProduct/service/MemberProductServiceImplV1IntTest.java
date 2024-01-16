package com.sportsecho.memberProduct.service;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.MemberTest;
import com.sportsecho.member.MemberTestUtil;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.memberProduct.MemberProductTestUtil;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.exception.MemberProductErrorCode;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.product.ProductTest;
import com.sportsecho.product.ProductTestUtil;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MemberProductServiceImplV1IntTest implements MemberTest, ProductTest {

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
    MemberProductRequestDto requestDto = MemberProductTestUtil.createRequestDto(2);

    @BeforeEach
    void setUp() {
        member = MemberTestUtil.getTestMember("customer@email.com", "pass");
        product = ProductTestUtil.getTestProduct();

        memberRepository.save(member);
        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        memberProductRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

    private MemberProduct createMemberProduct() {
        MemberProduct memberProduct = MemberProductTestUtil.getMemberProduct(member, product);
        memberProductRepository.save(memberProduct);
        return memberProduct;
    }

    @Nested
    @DisplayName("장바구니에 상품 추가 테스트")
    class addCartTest {
        @Test
        @DisplayName("장바구니 추가 성공 - 새 상품")
        void addCartTest_success_new() {
            //when
            MemberProductResponseDto responseDto = memberProductService.addCart(product.getId(), requestDto, member);

            //then
            assertEquals(requestDto.getProductsQuantity(), responseDto.getProductsQuantity());
            assertEquals(product.getPrice(), responseDto.getPrice());
            assertEquals(product.getTitle(), responseDto.getTitle());
        }

        @Test
        @DisplayName("장바구니 추가 성공 - 기존에 존재하던 상품")
        void addCartTest_success_old() {
            //given
            MemberProduct memberProduct = createMemberProduct();

            //when
            MemberProductResponseDto responseDto = memberProductService.addCart(product.getId(), requestDto, member);

            //then
            assertEquals(requestDto.getProductsQuantity() + memberProduct.getProductsQuantity(),
                    responseDto.getProductsQuantity());
            assertEquals(product.getPrice(), responseDto.getPrice());
            assertEquals(product.getTitle(), responseDto.getTitle());
        }

        @Test
        @DisplayName("장바구니 추가 실패 - 상품이 존재하지 않음")
        void addCartTest_fail_notFoundProduct() {
            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                memberProductService.addCart(10L, requestDto, member);
            });
            assertEquals(MemberProductErrorCode.NOT_FOUND_PRODUCT, e.getErrorCode());
        }
    }

    @Test
    @DisplayName("장바구니 조회 테스트")
    void Test() {
        //given
        MemberProduct memberProduct1 = createMemberProduct();
        MemberProduct memberProduct2 = createMemberProduct();
        MemberProduct memberProduct3 = createMemberProduct();

        //when
        List<MemberProductResponseDto> memberProductList = memberProductService.getCart(member);

        //then
        assertEquals(3, memberProductList.size());
        assertEquals(memberProduct1.getProduct().getTitle(), memberProductList.get(0).getTitle());
    }

    @Nested
    @DisplayName("장바구니 삭제 테스트")
    class deleteCart {
        @Test
        @DisplayName("단일 상품 삭제 성공")
        void deleteCartTest_success() {
            //given
            MemberProduct memberProduct = createMemberProduct();

            //when
            memberProductService.deleteCart(product.getId(), member);

            //then
            assertTrue(memberProductRepository.findById(memberProduct.getId()).isEmpty());
        }

        @Test
        @DisplayName("단일 상품 삭제 실패 - 장바구니에 상품이 없음")
        void deleteCartTest_fail() {
            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                memberProductService.deleteCart(product.getId(), member);

            });
            assertEquals(MemberProductErrorCode.NOT_FOUND_PRODUCT_IN_CART, e.getErrorCode());
        }

        @Test
        @DisplayName("전체 상품 삭제 성공")
        void deleteAllCartTest_success() {
            //given
            MemberProduct memberProduct1 = createMemberProduct();
            MemberProduct memberProduct2 = createMemberProduct();
            MemberProduct memberProduct3 = createMemberProduct();

            //when
            memberProductService.deleteAllCart(member);

            //then
            assertTrue(memberProductRepository.findById(memberProduct1.getId()).isEmpty());
            assertTrue(memberProductRepository.findById(memberProduct2.getId()).isEmpty());
            assertTrue(memberProductRepository.findById(memberProduct3.getId()).isEmpty());
        }
    }
}