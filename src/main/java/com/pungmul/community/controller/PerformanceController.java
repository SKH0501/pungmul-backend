package com.pungmul.community.controller;

import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.PerformanceCreateRequest;
import com.pungmul.community.dto.request.PerformanceUpdateRequest;
import com.pungmul.community.dto.response.PerformanceResponse;
import com.pungmul.community.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    // 목록 조회
    @GetMapping
    public ResponseEntity<Page<PerformanceResponse>> getList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean upcoming,
            @RequestParam(defaultValue = "performedAt") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                performanceService.getList(keyword, upcoming, sort, page, size));
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PerformanceResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(performanceService.getOne(id));
    }

    // 등록
    @PostMapping
    public ResponseEntity<PerformanceResponse> create(
            @RequestBody PerformanceCreateRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(performanceService.create(request, currentUser));
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<PerformanceResponse> update(
            @PathVariable Long id,
            @RequestBody PerformanceUpdateRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(performanceService.update(id, request, currentUser));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        performanceService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}