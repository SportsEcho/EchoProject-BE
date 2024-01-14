package com.sportsecho.product;

import com.sportsecho.product.dto.request.ProductRequestDto;
import com.sportsecho.product.entity.Product;

public interface ProductTest {

    String TEST_PRODUCT_TITLE = "test_product_title";
    String TEST_PRODUCT_CONTENT = "test_product_content";
    String TEST_PRODUCT_IMAGEURL = "testimageurl.com";
    int TEST_PRICE = 9900;
    int TEST_QUANTITY = 10;

    Product TEST_PRODUCT = Product.builder()
        .title(TEST_PRODUCT_TITLE)
        .content(TEST_PRODUCT_CONTENT)
        .imageUrl(TEST_PRODUCT_IMAGEURL)
        .price(TEST_PRICE)
        .quantity(TEST_QUANTITY)
        .build();
}
