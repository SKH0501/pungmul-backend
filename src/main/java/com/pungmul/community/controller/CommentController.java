package com.pungmul.community.controller;

import com.pungmul.community.dto.request.CommentCreateRequest;
import com.pungmul.community.dto.request.CommentUpdateRequest;
import com.pungmul.community.dto.response.CommentResponse;
import com.pungmul.community.repository.CommentRepository;
import com.pungmul.community.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    // 1. POST /api/comments
    @PostMapping()
    public ResponseEntity<CommentResponse> create(@RequestBody CommentCreateRequest request){
        CommentResponse commentResponse = commentService.create(request);
        return ResponseEntity.ok(commentResponse);
    }
    // 2. GET /api/comments?reviewId=1
    @GetMapping()
    public ResponseEntity<List<CommentResponse>> getList(@RequestParam Long reviewId){
        List<CommentResponse> list = commentService.getList(reviewId);
        return ResponseEntity.ok(list);
    }
    // 3. PATCH /api/comments/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestBody CommentUpdateRequest request){
        CommentResponse commentResponse = commentService.update(id, request);
        return ResponseEntity.ok(commentResponse);
    }
    // 4. DELETE /api/comments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
