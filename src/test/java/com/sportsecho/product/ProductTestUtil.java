package com.sportsecho.product;

import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.entity.Product;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductTestUtil implements ProductTest{

    public static Product getTestProduct() {
        return Product.builder()
            .title(TEST_PRODUCT_TITLE)
            .content(TEST_PRODUCT_CONTENT)
            .imageUrl(TEST_PRODUCT_IMAGEURL)
            .price(TEST_PRICE)
            .quantity(TEST_QUANTITY)
            .build();
    }

    public static ProductRequestDto createTestProductRequestDto(String productTitle,
        String productContent, String productImageUrl, int productPrice, int productQuantity) {
        ProductRequestDto productRequestDto = new ProductRequestDto();

        ReflectionTestUtils.setField(productRequestDto, "title", productTitle, String.class);
        ReflectionTestUtils.setField(productRequestDto, "content", productContent, String.class);
        ReflectionTestUtils.setField(productRequestDto, "imageUrl", productImageUrl, String.class);
        ReflectionTestUtils.setField(productRequestDto, "price", productPrice, int.class);
        ReflectionTestUtils.setField(productRequestDto, "quantity", productQuantity, int.class);
        return productRequestDto;
    }

}
