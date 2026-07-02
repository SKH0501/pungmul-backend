package com.pungmul.community.controller;

import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.ReviewCreateRequest;
import com.pungmul.community.dto.request.ReviewUpdateRequest;
import com.pungmul.community.dto.response.ReviewResponse;
import com.pungmul.community.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 1. 댓글 등록 ✅ Authentication 추가
    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @RequestBody ReviewCreateRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(reviewService.create(request, currentUser));
    }

    // 2. 공연별 댓글 목록
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getList(
            @RequestParam Long performanceId) {
        return ResponseEntity.ok(reviewService.getList(performanceId));
    }

    // 3. 댓글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getOne(id));
    }

    // 4. 댓글 수정 ✅ Authentication 추가
    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateOne(
            @PathVariable Long id,
            @RequestBody ReviewUpdateRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(reviewService.update(id, request, currentUser));
    }

    // 5. 댓글 삭제 ✅ Authentication 추가
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        reviewService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}