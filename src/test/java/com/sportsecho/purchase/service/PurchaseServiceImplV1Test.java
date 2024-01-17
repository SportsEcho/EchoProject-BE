package com.sportsecho.purchase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.MemberTest;
import com.sportsecho.member.MemberTestUtil;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.memberProduct.MemberProductTestUtil;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.product.ProductTest;
import com.sportsecho.product.ProductTestUtil;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import com.sportsecho.purchase.PurchaseTest;
import com.sportsecho.purchase.PurchaseTestUtil;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.exception.PurchaseErrorCode;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.repository.PurchaseProductRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class PurchaseServiceImplV1Test implements MemberTest, ProductTest, PurchaseTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    PurchaseProductRepository purchaseProductRepository;

    @Autowired
    MemberProductRepository memberProductRepository;

    @Autowired
    PurchaseServiceImplV1 purchaseService;

    Member member;
    Product product;
    MemberProduct memberProduct;
    PurchaseRequestDto requestDto = PurchaseTestUtil.getRequestDto();

    @BeforeEach
    void setUp() {
        member = MemberTestUtil.getTestMember(TEST_EMAIL, TEST_PASSWORD);
        product = ProductTestUtil.getTestProduct();
        memberProduct = MemberProductTestUtil.getMemberProduct(member, product);

        memberRepository.save(member);
        productRepository.save(product);
        memberProductRepository.save(memberProduct);
    }

    @AfterEach
    void tearDown() {
        memberProductRepository.deleteAll();
        purchaseProductRepository.deleteAll();
        purchaseRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Nested
    @DisplayName("구매 테스트")
    class purchaseTest {

        @Test
        @DisplayName("장바구니에 있던 상품 구매 성공")
        void purchaseTest_success() {
            //when
            PurchaseResponseDto responseDto = purchaseService.purchase(requestDto, member);

            //then
            assertEquals(product.getPrice() * memberProduct.getProductsQuantity(),
                responseDto.getTotalPrice());
            assertEquals(requestDto.getAddress(), responseDto.getAddress());
            assertEquals(product.getTitle(), responseDto.getResponseDtoList().get(0).getTitle());
            assertTrue(memberProductRepository.findByMemberId(member.getId()).isEmpty());
        }

        @Test
        @DisplayName("구매 실패 - 장바구니가 비어있음")
        void purchaseTest_fail_emptyCart() {
            //given
            memberProductRepository.deleteAll();

            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseService.purchase(requestDto, member);
            });
            assertEquals(PurchaseErrorCode.EMPTY_CART, e.getErrorCode());
        }

        @Test
        @DisplayName("구매 실패 - 상품 재고 없음")
        void purchaseTest_fail_outOfStock() {
            //given
            memberProductRepository.save(
                MemberProductTestUtil.getMemberProduct(member, product, 30)
            );

            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseService.purchase(requestDto, member);
            });
            assertEquals(PurchaseErrorCode.OUT_OF_STOCK, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("구매 목록 조회 테스트")
    class getPurchaseListTest {

        @Test
        @DisplayName("멤버의 구매 목록 조회 성공")
        void getPurchaseListTest_success() {
            //given
            purchaseService.purchase(requestDto, member);

            //when
            List<PurchaseResponseDto> responseDtoList = purchaseService.getPurchaseList(member);

            //then
            assertEquals(1, responseDtoList.size());
            assertEquals(requestDto.getAddress(), responseDtoList.get(0).getAddress());
            assertEquals(memberProduct.getProductsQuantity() * product.getPrice(),
                responseDtoList.get(0).getTotalPrice());
            assertEquals(product.getTitle(),
                responseDtoList.get(0).getResponseDtoList().get(0).getTitle());
            assertEquals(memberProduct.getProductsQuantity(),
                responseDtoList.get(0).getResponseDtoList().get(0).getProductsQuantity());
        }

        @Test
        @DisplayName("멤버의 구매 목록 조회 실패 - 구매 내역 없음")
        void getPurchaseListTest_fail() {
            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseService.getPurchaseList(member);
            });
            assertEquals(PurchaseErrorCode.EMPTY_PURCHASE_LIST, e.getErrorCode());
        }
    }

}