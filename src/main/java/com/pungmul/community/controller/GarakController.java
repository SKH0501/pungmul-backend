package com.pungmul.community.controller;

import com.pungmul.community.dto.request.GarakCreateRequest;
import com.pungmul.community.dto.response.GarakResponse;
import com.pungmul.community.service.GarakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garaks")
@RequiredArgsConstructor
public class GarakController {

    private final GarakService garakService;

    // 가락 등록
    @PostMapping
    public ResponseEntity<GarakResponse> create(@RequestBody GarakCreateRequest request) {
        GarakResponse response = garakService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 가락 목록
    @GetMapping
    public ResponseEntity<List<GarakResponse>> getList() {
        return ResponseEntity.ok(garakService.getList());
    }

    // 가락 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<GarakResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(garakService.getOne(id));
    }
}