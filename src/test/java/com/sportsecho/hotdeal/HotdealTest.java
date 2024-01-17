package com.sportsecho.hotdeal;

import java.time.LocalDateTime;

public interface HotdealTest {

    LocalDateTime TEST_START_DAY = LocalDateTime.now();
    LocalDateTime TEST_DUE_DAY = TEST_START_DAY.plusDays(10);
    int TEST_DEAL_QUANTITY = 100;
    int TEST_SALE = 50;


}
