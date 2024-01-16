package com.sportsecho.hotdeal;

import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.member.MemberTest;
import com.sportsecho.product.entity.Product;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class HotdealTestUtil implements MemberTest, HotdealTest {

    public static Hotdeal createHotdeal(LocalDateTime startDay, LocalDateTime dueDay, int dealQuantity, int sale, Product product) {
        return Hotdeal.builder()
            .startDay(startDay)
            .dueDay(dueDay)
            .dealQuantity(dealQuantity)
            .sale(sale)
            .product(product)
            .build();
    }

    public static HotdealRequestDto createTestHotdealReqeustDto(LocalDateTime startDay,
        LocalDateTime dueDay, int dealQuantity, int sale) {

        HotdealRequestDto hotdealRequestDto = new HotdealRequestDto();
        ReflectionTestUtils.setField(hotdealRequestDto, "startDay", startDay, LocalDateTime.class);
        ReflectionTestUtils.setField(hotdealRequestDto, "dueDay", dueDay, LocalDateTime.class);
        ReflectionTestUtils.setField(hotdealRequestDto, "dealQuantity", dealQuantity, int.class);
        ReflectionTestUtils.setField(hotdealRequestDto, "sale", sale, int.class);

        return hotdealRequestDto;
    }
}
