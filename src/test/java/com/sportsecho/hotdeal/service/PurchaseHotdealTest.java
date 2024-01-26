package com.sportsecho.hotdeal.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.hotdeal.HotdealTest;
import com.sportsecho.hotdeal.HotdealTestUtil;
import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.exception.HotdealErrorCode;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
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
    @Qualifier("V2")
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

    @Autowired
    RedisUtil redisUtil;

//    @BeforeEach
//    void setUp() {
//        member = memberRepository.save(
//            MemberTestUtil.getTestMember("customer", TEST_EMAIL, TEST_PASSWORD,
//                MemberRole.CUSTOMER));
//    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        hotdealRepository.deleteAll();
        purchaseProductRepository.deleteAll();
        purchaseRepository.deleteAll();
    }

    @Nested
    @DisplayName("단일 구매자의 구매 테스트")
    class SinglePurchaseTest {

        private Member member = memberRepository.save(
            MemberTestUtil.getTestMember("customer", TEST_EMAIL, TEST_PASSWORD,
                MemberRole.CUSTOMER));

        private Product product = productRepository.save(TEST_PRODUCT);
        private Hotdeal hotdeal = hotdealRepository.save(
            HotdealTestUtil.getHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                TEST_SALE, product));
        private PurchaseHotdealRequestDto requestDto = HotdealTestUtil.getTestPurchaseHotdealRequestDto(
            hotdeal.getId(), 3);

        @BeforeEach
        void setMemberPurchaseWait() {
            // 단일 유저 구매 대기열 등록
            redisUtil.addPurchaseHotdealMemberToQueueString(hotdeal.getId().toString(),
                member.getEmail(), System.currentTimeMillis());
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
    @DisplayName("구매 대기 테스트")
    class PurchaseWaitTest {

        private Product product = productRepository.save(TEST_PRODUCT);
        private Hotdeal hotdeal = hotdealRepository.save(
            HotdealTestUtil.getHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY * 10, // 10배로 늘림 1000개
                TEST_SALE, product));
        private PurchaseHotdealRequestDto requestDto = HotdealTestUtil.getTestPurchaseHotdealRequestDto(
            hotdeal.getId(), 3);
        private int waitCount = 10;

        @AfterEach
        void tearDown() {
            // 대기열 초기화
            redisUtil.deleteOldHotdealWaitSet(hotdeal.getId().toString());
        }

        @Test
        @DisplayName("성공 - 구매자가 구매 대기 목록에 추가되는지 확인")
        void waitHotdeal() {
            // given
            Member anotherMember = memberRepository.save(
                MemberTestUtil.getTestMember("customer", "another@another.com", TEST_PASSWORD,
                    MemberRole.CUSTOMER));
            Boolean beforeWait = redisUtil.isInWaitQueue(hotdeal.getId().toString(),
                anotherMember.getEmail());

            // when
            redisUtil.addPurchaseHotdealMemberToQueueString(hotdeal.getId().toString(),
                anotherMember.getEmail(), System.currentTimeMillis());

            // then
            assertFalse(beforeWait);
            assertTrue(
                redisUtil.isInWaitQueue(hotdeal.getId().toString(), anotherMember.getEmail()));
        }

        @Test
        @DisplayName("실패 - 대기목록에 없는 구매자의 구매시도")
        void purchaseHotdeal_fail() {
            // given
            Member anotherMember = memberRepository.save(
                MemberTestUtil.getTestMember("customer", "another@another.com", TEST_PASSWORD,
                    MemberRole.CUSTOMER));
            PurchaseHotdealRequestDto anotherRequestDto = HotdealTestUtil.getTestPurchaseHotdealRequestDto(
                hotdeal.getId(), 3);

            // when & then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.purchaseHotdeal(anotherMember, anotherRequestDto);
            });
            assertEquals(HotdealErrorCode.NOT_IN_WAIT_QUEUE, thrown.getErrorCode());
        }

        @Test
        @DisplayName("순차적대기 - SortedSet에 데이터가 순서대로 저장되는지 확인")
        void waitHotdeal_removeFromWaitList() {
            // given
            IntStream.range(0, waitCount).forEach(i -> {
                String uniqueEmail = i + TEST_EMAIL;
                memberRepository.save(
                    MemberTestUtil.getTestMember("customer", uniqueEmail, TEST_PASSWORD,
                        MemberRole.CUSTOMER));
                redisUtil.addPurchaseHotdealMemberToQueueString(hotdeal.getId().toString(),
                    uniqueEmail, System.currentTimeMillis());
            });

            StringBuilder orderedEmailNumbers = new StringBuilder();
            IntStream.range(0, waitCount).forEach(i -> {
                String uniqueEmail = i + TEST_EMAIL;
                Member member = memberRepository.findByEmail(uniqueEmail)
                    .orElseThrow(() -> new AssertionError("멤버를 찾을 수 없음: " + uniqueEmail));
                hotdealService.purchaseHotdeal(member, requestDto);
                redisUtil.removePurchaseRequestFromQueue(hotdeal.getId().toString(), uniqueEmail);
                orderedEmailNumbers.append(uniqueEmail, 0, 1);
            });

            // then
            assertEquals("0123456789", orderedEmailNumbers.toString());


        }

        @Test
        @DisplayName("멀티쓰레드 - SortedSet에 데이터가 순서대로 저장되는지 확인")
        void givenMultipleThreads_whenAddToSortedSet_thenShouldBeInOrder() throws InterruptedException {
            // given
            String key = "hotdeal:waitQueue";
            int numberOfThreads = 10;
            ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // when
            for (int i = 0; i < numberOfThreads; i++) {
                final double score = (double) i;
                service.execute(() -> {
                    try {
                        String memberValue = "member" + score;
                        redisUtil.addPurchaseHotdealMemberToQueueString(key, memberValue, score);
                        System.out.println("Thread with score " + score + " added member " + memberValue);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(60, TimeUnit.SECONDS);
            service.shutdown();

            // then
            List<String> membersInOrder = redisUtil.getOldHotdealWaitSet(key, numberOfThreads)
                .stream().toList();
            System.out.println("정렬전 Members in order: " + membersInOrder);

            for (int i = 0; i < numberOfThreads; i++) {
                String expectedMemberValue = "member" + (double) i; // Redis Sorted Set에 저장된 순서를 기대 값으로 사용
                assertEquals(expectedMemberValue, membersInOrder.get(i));
            }

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
                HotdealTestUtil.getHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));

            int beforeDealQuantity = hotdeal.getDealQuantity();

            int purchaseQuantity = 4;
            PurchaseHotdealRequestDto requestDto = HotdealTestUtil.getTestPurchaseHotdealRequestDto(
                hotdeal.getId(), purchaseQuantity);

            int numberOfThreads = 100;
            ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            AtomicInteger successfulPurchases = new AtomicInteger(0); // 성공적인 구매 요청 수를 추적

            // when
            for (int i = 0; i < numberOfThreads; i++) {
                final int threadNum = i;
                service.execute(() -> {
                    try {
                        // 각 쓰레드마다 고유한 이메일을 사용하여 Member 객체 생성
                        Member threadMember = memberRepository.save(
                            MemberTestUtil.getTestMember("customer" + threadNum,
                                threadNum + TEST_EMAIL, TEST_PASSWORD,
                                MemberRole.CUSTOMER));
                        // ...
                        redisUtil.addPurchaseHotdealMemberToQueueString(
                            String.valueOf(hotdeal.getId()), threadMember.getEmail(),
                            System.currentTimeMillis());
                        hotdealService.purchaseHotdeal(threadMember, requestDto);
                        successfulPurchases.incrementAndGet(); // 성공적인 구매 요청을 카운트
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
            int expectedRemainingQuantity = beforeDealQuantity - successfulPurchases.get() * purchaseQuantity;
            assertEquals(expectedRemainingQuantity, foundHotdeal.getDealQuantity());
        }

    }

}