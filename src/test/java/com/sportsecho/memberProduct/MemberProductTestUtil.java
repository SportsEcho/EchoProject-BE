package com.sportsecho.memberProduct;

import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.product.entity.Product;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberProductTestUtil implements MemberProductTest {

    public static MemberProduct getMemberProduct(Member member, Product product) {
        return MemberProduct.builder()
                .member(member)
                .product(product)
                .productsQuantity(TEST_QUANTITY)
                .build();
    }

    public static MemberProductRequestDto getRequestDto(int quantity) {
        MemberProductRequestDto requestDto = new MemberProductRequestDto();
        ReflectionTestUtils.setField(requestDto, "productsQuantity", quantity);
        return requestDto;
    }
}
