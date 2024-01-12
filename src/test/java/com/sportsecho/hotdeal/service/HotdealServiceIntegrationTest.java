package com.sportsecho.hotdeal.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.product.repository.ProductRepository;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotdealServiceIntegrationTest {

    @Autowired
    @Qualifier("V1") HotdealService hotdealService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    HotdealRepository hotdealRepository;


    @BeforeAll
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("핫딜 생성")
    class createHotdeal {
        @Test
        @DisplayName("성공 - 핫딜 생성")
        void createHotdeal() {
            // Given

            // When

            // Then
        }

    }


    @Test
    void getHotdeal() {
    }

    @Test
    void getHotdealList() {
    }

    @Test
    void updateHotdeal() {
    }

    @Test
    void decreaseHotdealDealQuantity() {
    }

    @Test
    void deleteHotdeal() {
    }
}