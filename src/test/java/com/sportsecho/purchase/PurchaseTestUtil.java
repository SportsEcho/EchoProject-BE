package com.sportsecho.purchase;

import com.sportsecho.purchase.dto.PurchaseRequestDto;
import org.springframework.test.util.ReflectionTestUtils;

public class PurchaseTestUtil implements PurchaseTest {

    public static PurchaseRequestDto getRequestDto() {
        PurchaseRequestDto requestDto = new PurchaseRequestDto();

        ReflectionTestUtils.setField(requestDto, "address", TEST_ADDRESS);
        ReflectionTestUtils.setField(requestDto, "phone", TEST_PHONE);

        return requestDto;
    }
}
