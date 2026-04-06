package com.pungmul.community.controller;

import com.pungmul.community.domain.Performance;
import com.pungmul.community.dto.request.PerformanceCreateRequest;
import com.pungmul.community.dto.request.PerformanceUpdateRequest;
import com.pungmul.community.dto.response.PerformanceResponse;
import com.pungmul.community.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/performances")
public class PerformanceController {
    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    // 전체 리스트 보기
    @GetMapping
    public ResponseEntity<List<PerformanceResponse>> getList() {
        List<PerformanceResponse> performances = performanceService.getList();
        return ResponseEntity.ok(performances);
    }
    // 상세 조회 - URL에서 {id} 받기
    @GetMapping("/{id}")
    public ResponseEntity<PerformanceResponse> getOne(@PathVariable Long id) {
        PerformanceResponse one = performanceService.getOne(id);
        return ResponseEntity.ok(one);
    }

    // 등록 - Body에서 데이터 받기
    @PostMapping
    public ResponseEntity<PerformanceResponse> create(
            @RequestBody PerformanceCreateRequest request) {
            PerformanceResponse response = performanceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<PerformanceResponse> update(
            @PathVariable Long id, @RequestBody PerformanceUpdateRequest request) {
            PerformanceResponse response = performanceService.update(id, request);
            return ResponseEntity.ok(response);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        performanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
