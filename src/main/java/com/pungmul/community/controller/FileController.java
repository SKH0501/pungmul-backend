package com.pungmul.community.controller;

import com.pungmul.community.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class FileController {

    private final S3Service s3Service;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) {
        return s3Service.upload(file);
    }

    // ✅ Presigned URL 발급
    @GetMapping("/presigned")
    public ResponseEntity<Map<String, String>> getPresignedUrl(
            @RequestParam String contentType,
            @RequestParam String fileType) {
        return ResponseEntity.ok(s3Service.generatePresignedUrl(contentType, fileType));
    }
}