package com.sportsecho.hotdeal.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.hotdeal.HotdealTest;
import com.sportsecho.hotdeal.HotdealTestUtil;
import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
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
public class HotdealServiceIntegrationTest implements MemberTest, ProductTest, HotdealTest {

    @Autowired
    @Qualifier("V1")
    HotdealService hotdealService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    HotdealRepository hotdealRepository;

    private Member customerMember, adminMember;

    private Product product;


    @BeforeEach
    void setUp() {
        hotdealRepository.deleteAll();

        customerMember = memberRepository.save(
            MemberTestUtil.getTestMember("customer", TEST_EMAIL, TEST_PASSWORD,
                MemberRole.CUSTOMER));
        adminMember = memberRepository.save(
            MemberTestUtil.getTestMember("admin", ANOTHER_TEST_EMAIL, TEST_PASSWORD,
                MemberRole.ADMIN));
        product = productRepository.save(TEST_PRODUCT);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        productRepository.deleteAll();
        hotdealRepository.deleteAll();
    }

    @Nested
    @DisplayName("핫딜 생성")
    class CreateHotdeal {

        @Test
        @DisplayName("성공 - 핫딜 생성")
        void successCreateHotdeal() {
            // Given
            HotdealRequestDto requestDto = HotdealTestUtil.createTestHotdealReqeustDto(
                TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY, TEST_SALE);

            // When
            HotdealResponseDto result = hotdealService.createHotdeal(adminMember, product.getId(),
                requestDto);

            // Then
            assertNotNull(result);
            assertEquals(requestDto.getStartDay(), result.getStartDay());

        }

        @Test
        @DisplayName("실패 - 권한 없는 사용자")
        void failCreateHotdealUnauthorized() {
            HotdealRequestDto requestDto = HotdealTestUtil.createTestHotdealReqeustDto(
                TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY, TEST_SALE);

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.createHotdeal(customerMember, product.getId(), requestDto);
            });
            assertEquals(HotdealErrorCode.NO_AUTHORIZATION, thrown.getErrorCode());
        }
    }

    @Nested
    @DisplayName("핫딜 조회")
    class getHotdeal {

        @Test
        @DisplayName("성공 - 핫딜 조회")
        void getHotdealSuccess() {
            // Given
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));

            // When
            HotdealResponseDto responseDto = hotdealService.getHotdeal(hotdeal.getId());

            // Then
            assertNotNull(responseDto);
            assertEquals(hotdeal.getStartDay().withNano(0), responseDto.getStartDay()
                .withNano(0)); // LocalDateTime의 equals는 nano까지 비교하기 때문에 withNano(0)으로 비교
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 핫딜 조회")
        void getHotdealFail() {
            // Given
            Long invalidHotdealId = 999L;

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.getHotdeal(invalidHotdealId);
            });
            assertEquals(HotdealErrorCode.NOT_FOUND_HOTDEAL, thrown.getErrorCode());
        }

    }

    @Nested
    @DisplayName("핫딜 목록 조회")
    class getHotdealList {

        @Test
        @DisplayName("성공 - 핫딜 목록 조회")
        void getHotdealListSuccess() {
            // Given
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));

            // When
            HotdealResponseDto responseDto = hotdealService.getHotdeal(hotdeal.getId());

            // Then
            assertNotNull(responseDto);
            assertEquals(hotdeal.getStartDay().withNano(0), responseDto.getStartDay()
                .withNano(0)); // LocalDateTime의 equals는 nano까지 비교하기 때문에 withNano(0)으로 비교
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 핫딜 조회")
        void getHotdealListFail() {
            // Given
            Long invalidHotdealId = 999L;

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.getHotdeal(invalidHotdealId);
            });
            assertEquals(HotdealErrorCode.NOT_FOUND_HOTDEAL, thrown.getErrorCode());
        }

    }

    @Nested
    @DisplayName("핫딜 수정")
    class updateHotdeal {

        private final UpdateHotdealInfoRequestDto updateHotdealInfoRequestDto = HotdealTestUtil.createTestUpdateHotdealInfoRequestDto(
            TEST_START_DAY.plusDays(1), TEST_DUE_DAY.plusDays(1), TEST_DEAL_QUANTITY + 1,
            TEST_SALE + 1);

        @Test
        @DisplayName("성공 - 핫딜 수정")
        void updateHotdealSuccess() {
            // Given
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));
            HotdealRequestDto requestDto = HotdealTestUtil.createTestHotdealReqeustDto(
                TEST_START_DAY.plusDays(1), TEST_DUE_DAY.plusDays(1), TEST_DEAL_QUANTITY + 1,
                TEST_SALE + 1);

            // When
            HotdealResponseDto responseDto = hotdealService.updateHotdeal(adminMember,
                hotdeal.getId(), updateHotdealInfoRequestDto);

            // Then
            assertNotNull(responseDto);
            assertEquals(requestDto.getStartDay(), responseDto.getStartDay());
            assertEquals(requestDto.getDueDay(), responseDto.getDueDay());
            assertEquals(requestDto.getDealQuantity(), responseDto.getDealQuantity());
            assertEquals(requestDto.getSale(), responseDto.getSale());
        }

        @Test
        @DisplayName("실패 - 권한 없는 사용자")
        void updateHotdealFailUnauthorized() {
            // Given
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.updateHotdeal(customerMember, hotdeal.getId(), updateHotdealInfoRequestDto);
            });
            assertEquals(HotdealErrorCode.NO_AUTHORIZATION, thrown.getErrorCode());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 핫딜")
        void updateHotdealFailNotFoundHotdeal() {
            // Given
            Long invalidHotdealId = 999L;

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.updateHotdeal(adminMember, invalidHotdealId, updateHotdealInfoRequestDto);
            });
            assertEquals(HotdealErrorCode.NOT_FOUND_HOTDEAL, thrown.getErrorCode());
        }
    }

    @Nested
    @DisplayName("핫딜 삭제")
    class deleteHotdeal {

        @Test
        @DisplayName("성공 - 핫딜 삭제")
        void deleteHotdealSuccess() {
            // Given
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));

            // When

            hotdealService.deleteHotdeal(adminMember, hotdeal.getId());

            // Then
            assertNotNull(hotdeal.getId());
            assertTrue(hotdealRepository.findById(hotdeal.getId()).isEmpty());
        }

        @Test
        @DisplayName("실패 - 권한 없는 사용자")
        void deleteHotdealFailUnauthorized() {
            // Given
            Hotdeal hotdeal = hotdealRepository.save(
                HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
                    TEST_SALE, product));

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.deleteHotdeal(customerMember, hotdeal.getId());
            });
            assertEquals(HotdealErrorCode.NO_AUTHORIZATION, thrown.getErrorCode());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 핫딜")
        void deleteHotdealFailNotFoundHotdeal() {
            // Given
            Long invalidHotdealId = 999L;

            // When && Then
            GlobalException thrown = assertThrows(GlobalException.class, () -> {
                hotdealService.deleteHotdeal(adminMember, invalidHotdealId);
            });
            assertEquals(HotdealErrorCode.NOT_FOUND_HOTDEAL, thrown.getErrorCode());
        }
    }

//    @Nested
//    @DisplayName("핫딜 구매")

}