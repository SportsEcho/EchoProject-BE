package com.sportsecho.global.util.s3.controller;

import com.sportsecho.global.util.s3.service.FileUploadService;
import com.sportsecho.member.entity.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/image")
    public String uploadImage(
        @RequestParam(value = "file") MultipartFile file,
        @RequestParam(value = "identifier") String identifier,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return fileUploadService.uploadFile(memberDetails.getMember(), file, identifier);
    }

    @PostMapping("/products/{productId}/images")
    public String uploadProductImage(
        @RequestParam(value = "file") MultipartFile file,
        @RequestParam(value = "identifier") String identifier,
        @PathVariable Long productId,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return fileUploadService.uploadProductFile(memberDetails.getMember(), file, identifier,
            productId);
    }
}
