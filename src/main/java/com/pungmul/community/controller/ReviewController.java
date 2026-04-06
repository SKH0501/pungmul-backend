package com.pungmul.community.controller;

import com.pungmul.community.dto.request.ReviewCreateRequest;
import com.pungmul.community.dto.request.ReviewUpdateRequest;
import com.pungmul.community.dto.response.ReviewResponse;
import com.pungmul.community.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 1. 후기 등록
    // POST /api/reviews
    @PostMapping
    public ResponseEntity<ReviewResponse> create
            (@RequestBody ReviewCreateRequest request){
        ReviewResponse reviewResponse = reviewService.create(request);
        return ResponseEntity.ok(reviewResponse);
    }
    // 2. 공연별 후기 목록
    // GET /api/reviews?performanceId=1
    // 힌트: @RequestParam Long performanceId
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getList
        (@RequestParam Long performanceId ){
        List<ReviewResponse> reviewResponse = reviewService.getList(performanceId);
        return ResponseEntity.ok(reviewResponse);
    }

    // 3. 후기 단건 조회
    // GET /api/reviews/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getOne(@PathVariable Long id){
        ReviewResponse reviewResponse = reviewService.getOne(id);
        return ResponseEntity.ok(reviewResponse);
    }


    // 4. 후기 수정
    // PATCH /api/reviews/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateOne
    (@PathVariable Long id, @RequestBody ReviewUpdateRequest request){
        ReviewResponse reviewResponse = reviewService.update(id, request);
        return ResponseEntity.ok(reviewResponse);
    }
    // 5. 후기 삭제
    // DELETE /api/reviews/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id){
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}