package com.sportsecho.hotdeal.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.hotdeal.HotdealTest;
import com.sportsecho.hotdeal.HotdealTestUtil;
import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
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
import com.sportsecho.product.ProductTestUtil;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

//    @Nested
//    @DisplayName("핫딜 조회")
//    class getHotdeal {
//
//        @Test
//        void getHotdealSuccess() {
//            // Given
//            Hotdeal hotdeal = hotdealRepository.save(HotdealTestUtil.createHotdeal(TEST_START_DAY, TEST_DUE_DAY, TEST_DEAL_QUANTITY,
//                TEST_SALE, product));
//
//            // When
//            HotdealResponseDto responseDto = hotdealService.getHotdeal(hotdeal.getId());
//
//            // Then
//            assertNotNull(responseDto);
//            assertEquals(hotdeal.getStartDay(), responseDto.getStartDay());
//        }
//    }

}