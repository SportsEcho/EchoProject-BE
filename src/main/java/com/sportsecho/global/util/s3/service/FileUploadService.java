package com.sportsecho.global.util.s3.service;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.global.util.s3.S3Uploader;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.repository.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Uploader s3Uploader;

    public String uploadFile(Member member, MultipartFile file, String identifier) {

//        if (member.getRole().equals(MemberRole.CUSTOMER)) {
//            throw new GlobalException(ProductErrorCode.NO_AUTHORIZATION);
//        }

        UUID uuid = UUID.randomUUID();
        String fileName = identifier + uuid;

        return s3Uploader.upload(file, fileName);
    }
}
