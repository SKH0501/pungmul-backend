package com.pungmul.community.controller;

import com.pungmul.community.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class FileController {

    private final S3Service s3Service;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) {
        return s3Service.upload(file);
    }
}