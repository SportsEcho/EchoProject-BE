package com.sportsecho.hotdeal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.sportsecho.common.redis.RedisUtil;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PurchaseHotdealTestV2 implements MemberTest, ProductTest, HotdealTest, PurchaseTest {

    @Autowired
    HotdealServiceImplV2 hotdealService;

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

    @Autowired
    RedisUtil redisUtil;

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

        @AfterEach
        void tearDown() {
            purchaseRepository.deleteAll();
        }

        private Product product = productRepository.save(TEST_PRODUCT);
        private Hotdeal hotdeal = hotdealRepository.save(
            HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                TEST_SALE, product));

        private PurchaseHotdealRequestDto requestDto = HotdealTestUtil.createTestPurchaseHotdealRequestDto(
            hotdeal.getId(), 3);

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
    class MultipleUserPurchaseTest {

        @Test
        @DisplayName("redis sorted set 테스트 - 유저 대기열 생성 -> 접근 순서대로 구매")
        void purchaseHotdealWithSortedset() throws InterruptedException {

            // given
            Product product = productRepository.save(TEST_PRODUCT);
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, 50,
                    TEST_SALE, product));

            int purchaseQuantity = 1;
            PurchaseHotdealRequestDto requestDto = HotdealTestUtil.createTestPurchaseHotdealRequestDto(
                hotdeal.getId(), purchaseQuantity);

            hotdealService.hotdealSetting(hotdeal.getId());
            memberRepository.deleteAll();

            int numberOfThreads = 100;
            ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // when
            for (int i = 0; i < numberOfThreads; i++) {
                service.execute(() -> {
                    try {
                        member = memberRepository.save(
                            MemberTestUtil.getTestMember("customer", TEST_EMAIL,
                                TEST_PASSWORD, MemberRole.CUSTOMER));
                        hotdealService.purchaseHotdeal(member, requestDto);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(60, TimeUnit.SECONDS);

            while (true) {
                if (redisUtil.getSize(hotdeal.getId()) == 0) {
                    break;
                }
            }

            service.shutdown();
            Thread.sleep(1000);

            // then
            Hotdeal foundHotdeal = hotdealRepository.findById(hotdeal.getId()).orElse(null);
            assertEquals(0, foundHotdeal.getDealQuantity());

            redisUtil.deleteAll(hotdeal.getId());
        }

    }

}