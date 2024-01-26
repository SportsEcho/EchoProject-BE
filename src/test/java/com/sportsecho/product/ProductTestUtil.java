package com.sportsecho.product;

import com.sportsecho.product.dto.ProductRequestDto;
import com.sportsecho.product.entity.Product;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductTestUtil implements ProductTest {

    public static Product getTestProduct() {
        return Product.builder()
            .title(TEST_PRODUCT_TITLE)
            .content(TEST_PRODUCT_CONTENT)
            .price(TEST_PRICE)
            .quantity(TEST_QUANTITY)
            .build();
    }

    public static ProductRequestDto createTestProductRequestDto(String productTitle,
        String productContent, int productPrice, int productQuantity) {
        ProductRequestDto productRequestDto = new ProductRequestDto();

        ReflectionTestUtils.setField(productRequestDto, "title", productTitle, String.class);
        ReflectionTestUtils.setField(productRequestDto, "content", productContent, String.class);
        ReflectionTestUtils.setField(productRequestDto, "price", productPrice, int.class);
        ReflectionTestUtils.setField(productRequestDto, "quantity", productQuantity, int.class);
        return productRequestDto;
    }

}
