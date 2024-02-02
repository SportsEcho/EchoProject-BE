package com.sportsecho.hotdeal.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sportsecho.hotdeal.HotdealTest;
import com.sportsecho.hotdeal.HotdealTestUtil;
import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.member.MemberTest;
import com.sportsecho.member.MemberTestUtil;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.product.ProductTest;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import com.sportsecho.purchase.PurchaseTest;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.repository.PurchaseProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PurchaseHotdealTest implements MemberTest, ProductTest, HotdealTest, PurchaseTest {

    @Autowired
    @Qualifier("V1")
    HotdealService hotdealService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    HotdealRepository hotdealRepository;

    @Autowired
    PurchaseProductRepository purchaseProductRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(
            MemberTestUtil.getTestMember("customer", TEST_EMAIL, TEST_PASSWORD,
                MemberRole.CUSTOMER));
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        hotdealRepository.deleteAll();
        purchaseProductRepository.deleteAll();
    }

    @Nested
    @DisplayName("단일 구매자의 구매 테스트")
    class SinglePurchaseTest {

        private Product product;
        private Hotdeal hotdeal;
        private PurchaseHotdealRequestDto requestDto;

        @BeforeEach
        void setUp() {
            product = productRepository.save(
                Product.builder()
                    .title(TEST_PRODUCT_TITLE)
                    .content(TEST_PRODUCT_CONTENT)
                    .price(TEST_PRICE)
                    .quantity(TEST_QUANTITY)
                    .build()
            );
            hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));
            requestDto = HotdealTestUtil.createTestPurchaseHotdealRequestDto(
                hotdeal.getId(), 3);
        }

        @AfterEach
        void tearDown() {
            purchaseRepository.deleteAll();
        }

        @Test
        @DisplayName("성공 - 구매자가 구매한 상품이 구매 목록에 추가되는지 확인")
        void purchaseHotdeal() {
            hotdealService.purchaseHotdeal(member, requestDto);
            assertFalse(purchaseProductRepository.findAll().isEmpty());
        }

        @Test
        @DisplayName("성공 - 구매자가 구매한 핫딜의 한정 수량이 n만큼 감소하는지 확인")
        void purchaseHotdeal_decreaseDealQuantity() {
            // given
            int beforeDealQuantity = hotdeal.getDealQuantity();

            // when
            hotdealService.purchaseHotdeal(member, requestDto);

            // then
            Hotdeal foundHotdeal = hotdealRepository.findById(hotdeal.getId())
                .orElseThrow(() -> new AssertionError("핫딜을 찾을 수 없음"));
            assertEquals(beforeDealQuantity - requestDto.getQuantity(),
                foundHotdeal.getDealQuantity());
        }

        @Test
        @DisplayName("성공 - 구매자가 구매한 핫딜의 총 금액이 정확한지 확인")
        void purchaseHotdeal_checkTotalPrice() {
            int salePrice = product.getPrice() - (product.getPrice() * hotdeal.getSale() / 100);

            // when
            hotdealService.purchaseHotdeal(member, requestDto);

            // then
            assertEquals(salePrice, purchaseRepository.findAll().get(0).getTotalPrice());

        }

    }

    @Nested
    @DisplayName("다중 구매자의 구매 테스트")
    class MultipleUserPurchaseTets {

        @Test
        @DisplayName("데이터 정합성 테스트 - 핫딜의 한정 수량이 정확히 감소하는지 확인")
        void purchaseHotdealWithPessimisticLock() throws InterruptedException {
            // given
            Product product = productRepository.save(TEST_PRODUCT);
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));

            int beforeDealQuantity = hotdeal.getDealQuantity();

            int purchaseQuantity = 3;
            PurchaseHotdealRequestDto requestDto = HotdealTestUtil.createTestPurchaseHotdealRequestDto(
                hotdeal.getId(), purchaseQuantity);

            int numberOfThreads = 10;
            ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // when
            for (int i = 0; i < numberOfThreads; i++) {
                service.execute(() -> {
                    try {
                        hotdealService.purchaseHotdeal(member, requestDto);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(60, TimeUnit.SECONDS);
            service.shutdown();

            // then
            Hotdeal foundHotdeal = hotdealRepository.findById(hotdeal.getId())
                .orElseThrow(() -> new AssertionError("핫딜을 찾을 수 없음"));
            assertEquals(beforeDealQuantity - numberOfThreads * purchaseQuantity,
                foundHotdeal.getDealQuantity()); // 100 - 10 * 3 = 70
        }

    }

}