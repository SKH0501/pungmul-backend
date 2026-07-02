package com.pungmul.community.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 기존 업로드 메서드
    public String upload(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

            return amazonS3.getUrl(bucket, fileName).toString();

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패");
        }
    }

    // ✅ Presigned URL 발급 (FE에서 S3 직접 업로드)
    public Map<String, String> generatePresignedUrl(String contentType, String fileType) {
        // 파일 타입에 따라 폴더 구분
        String folder = contentType.startsWith("video") ? "videos" : "images";
        String fileName = folder + "/" + UUID.randomUUID() + "." + fileType;

        // 10분 후 만료
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10);

        GeneratePresignedUrlRequest presignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        presignedUrlRequest.setContentType(contentType);

        URL presignedUrl = amazonS3.generatePresignedUrl(presignedUrlRequest);

        // S3에 저장될 최종 URL
        String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

        return Map.of(
                "presignedUrl", presignedUrl.toString(),  // FE가 이 URL로 PUT 요청
                "fileUrl", fileUrl                         // DB에 저장할 URL
        );
    }
}