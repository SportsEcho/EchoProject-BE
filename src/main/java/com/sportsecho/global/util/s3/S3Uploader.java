package com.sportsecho.global.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String upload(MultipartFile file, String filename) {
        File fileObj = converMultiPartFileToFile(file);
        amazonS3Client.putObject(new PutObjectRequest(bucket, filename, fileObj));
        fileObj.delete();

        String fileurl = amazonS3Client.getUrl(bucket, filename).toString();

        return fileurl;
    }

    private File converMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            log.error("파일 변환 실패 : ", e);
        }
        return convertedFile;
    }

    public void deleteFile(String filename) {
        amazonS3Client.deleteObject(bucket, filename);
    }
}
