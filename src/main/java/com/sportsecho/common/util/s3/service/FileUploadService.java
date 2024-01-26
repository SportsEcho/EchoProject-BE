package com.sportsecho.common.util.s3.service;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.common.util.s3.S3Uploader;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.entity.ProductImage;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.repository.ProductImageRepository;
import com.sportsecho.product.repository.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Uploader s3Uploader;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public String uploadFile(Member member, MultipartFile file, String identifier) {

//        if (member.getRole().equals(MemberRole.CUSTOMER)) {
//            throw new GlobalException(ProductErrorCode.NO_AUTHORIZATION);
//        }

        UUID uuid = UUID.randomUUID();
        String fileName = identifier + uuid;

        return s3Uploader.upload(file, fileName);
    }

    @Transactional
    public String uploadProductFile(Member member, MultipartFile file, String identifier,
        Long productId) {

        if (member.getRole().equals(MemberRole.CUSTOMER)) {
            throw new GlobalException(ProductErrorCode.NO_AUTHORIZATION);
        }

        Product product = productRepository.findById(productId).orElseThrow(() ->
            new GlobalException(ProductErrorCode.NOT_FOUND_PRODUCT)
        );

        UUID uuid = UUID.randomUUID();
        String fileName = identifier + uuid;
        String fileUrl = s3Uploader.upload(file, fileName);

        ProductImage productImage = ProductImage.builder()
            .product(product)
            .imageUrl(fileUrl)
            .build();
        productImageRepository.save(productImage);

        product.getProductImageList().add(productImage);
        productRepository.save(product);

        return fileUrl;
    }
}
