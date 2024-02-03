package com.sportsecho.purchase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.member.MemberTest;
import com.sportsecho.member.MemberTestUtil;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.memberProduct.MemberProductTest;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
            int productQuantity = product.getQuantity();
            PurchaseResponseDto responseDto = purchaseService.purchase(requestDto, member);

            //then
            assertEquals(product.getPrice() * memberProduct.getProductsQuantity(),
                responseDto.getTotalPrice());
            assertEquals(requestDto.getAddress(), responseDto.getAddress());
            assertEquals(product.getTitle(),
                responseDto.getPurchaseProductList().get(0).getTitle());
            assertTrue(memberProductRepository.findByMemberId(member.getId()).isEmpty());
            assertEquals(productQuantity - memberProduct.getProductsQuantity(),
                productRepository.findAll().get(0).getQuantity());
        }

        /**
         * 사용자가 자신의 장바구니에 있는 상품의 구매 버튼을 눌렀을때 발생하는 동시성 이슈에 대한 테스트
         * 상품 구매 중 상품 version이 바뀌었다면 동시성 처리를 위해 ObjectOptimisticLockingFailureException 발생
         * 낙관적락을 이용해 동시성 제어가 잘 이루어지는지 확인하고 데이터 정합성 검증
         * */
        @Test
        @DisplayName("구매 실패 - 동일 상품에 다수의 구매 요청이 들어오는 경우")
        void purchaseTest_fail_concurrency() throws InterruptedException {
            //given
            int numberOfThreads = 10;
            int beforeQuantity = product.getQuantity();

            /* 테스트용 member 생성 */
            Member[] members = new Member[numberOfThreads];
            for (int i = 0; i < numberOfThreads; i++) {
                members[i] = memberRepository.save(
                    MemberTestUtil.getTestMember(
                        TEST_MEMBER_NAME,
                        TEST_PASSWORD,
                        TEST_EMAIL + i,
                        MemberRole.CUSTOMER
                    )
                );
            }

            /* 테스트용 장바구니 생성 및 저장 */
            for (int i = 0; i < numberOfThreads; i++) {
                memberProductRepository.save(
                    MemberProductTestUtil.getMemberProduct(members[i], product)
                );
            }

            /* exception count를 위한 boolean flag */
            boolean[] exceptionFlag = new boolean[members.length];

            ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            //when
            for (int i = 0; i < numberOfThreads; i++) {
                final int idx = i;
                service.execute(() -> {
                    try {
                        purchaseService.purchase(requestDto, members[idx]);
                    /* OptimisticLock과 재고부족으로 발생하는 예외처리 */
                    } catch(ObjectOptimisticLockingFailureException | GlobalException e) {
                        exceptionFlag[idx] = true;
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            service.shutdown();

            int exceptionCount = 0;
            for (boolean flag : exceptionFlag) {
                if(flag) exceptionCount++;
            }

            //then
            /* 테스트클래스의 product와 서비스 로직의 memberProduct.getProduct()는 quantity의 더티체킹이 진행되지 않음 */
            Product findProduct = productRepository.findById(product.getId()).orElse(null);
            assertNotNull(findProduct);

            /* 데이터 정합성 검증 */
            int expectQuantity = beforeQuantity -
                (numberOfThreads-exceptionCount)*MemberProductTest.TEST_QUANTITY;
            int actualQuantity = findProduct.getQuantity();
            assertEquals(expectQuantity, actualQuantity);
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

            memberProduct = MemberProductTestUtil.getMemberProduct(member, product);
            memberProductRepository.save(memberProduct);
            purchaseService.purchase(requestDto, member);

            //when
            List<PurchaseResponseDto> responseDtoList = purchaseService.getPurchaseList(member);

            //then
            assertEquals(2, responseDtoList.size());

            assertEquals(requestDto.getAddress(), responseDtoList.get(0).getAddress());
            assertEquals(memberProduct.getProductsQuantity() * product.getPrice(),
                responseDtoList.get(0).getTotalPrice());
            assertEquals(product.getTitle(),
                responseDtoList.get(0).getPurchaseProductList().get(0).getTitle());
            assertEquals(memberProduct.getProductsQuantity(),
                responseDtoList.get(0).getPurchaseProductList().get(0).getProductsQuantity());

            assertEquals(requestDto.getAddress(), responseDtoList.get(1).getAddress());
            assertEquals(product.getTitle(),
                responseDtoList.get(1).getPurchaseProductList().get(0).getTitle());
        }

        @Test
        @DisplayName("멤버의 구매 목록 조회 실패 - 구매 내역 없음")
        void getPurchaseListTest_fail() {
            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseService.getPurchaseList(member);
            });
            assertEquals(PurchaseErrorCode.NOT_FOUND_PURCHASE, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("구매 취소 테스트")
    class cancelPurchaseTest {

        @Test
        @DisplayName("구매 취소 성공")
        void cancelPurchaseTest_success() {
            //given
            PurchaseResponseDto responseDto = purchaseService.purchase(requestDto, member);
            Long purchaseId = responseDto.getId();

            //when
            purchaseService.cancelPurchase(purchaseId, member);

            //then
            assertTrue(purchaseRepository.findById(purchaseId).isEmpty());
            assertTrue(purchaseProductRepository.findAll().isEmpty());
            assertEquals(product.getQuantity(), productRepository.findAll().get(0).getQuantity());
        }

        @Test
        @DisplayName("구매 취소 실패 - 권한 없음")
        void cancelPurchaseTest_fail_accessDenied() {
            //given
            PurchaseResponseDto responseDto = purchaseService.purchase(requestDto, member);
            Member newMember = MemberTestUtil.getTestMember(TEST_EMAIL, TEST_PASSWORD);
            memberRepository.save(newMember);

            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseService.cancelPurchase(responseDto.getId(), newMember);
            });
            assertEquals(PurchaseErrorCode.ACCESS_DENIED, e.getErrorCode());
        }

        @Test
        @DisplayName("구매 취소 실패 - 구매 내역 없음")
        void cancelPurchaseTest_fail_notFound() {
            //when - then
            GlobalException e = assertThrows(GlobalException.class, () -> {
                purchaseService.cancelPurchase(1L, member);
            });
            assertEquals(PurchaseErrorCode.NOT_FOUND_PURCHASE, e.getErrorCode());
        }
    }
}