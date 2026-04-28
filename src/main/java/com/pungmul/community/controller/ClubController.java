package com.pungmul.community.controller;

import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.ClubCreateRequest;
import com.pungmul.community.dto.request.ClubUpdateRequest;
import com.pungmul.community.dto.response.ClubResponse;
import com.pungmul.community.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    // 1. 동아리 등록 ✅ Authentication 추가
    @PostMapping
    public ResponseEntity<ClubResponse> addClub(
            @RequestBody ClubCreateRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clubService.create(request, currentUser));
    }

    // 2. 동아리 목록
    @GetMapping
    public ResponseEntity<List<ClubResponse>> getList() {
        return ResponseEntity.ok(clubService.getList());
    }

    // 3. 동아리 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ClubResponse> getClubById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getOne(id));
    }

    // 4. 동아리 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ClubResponse> updateClub(
            @PathVariable Long id,
            @RequestBody ClubUpdateRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clubService.update(id, request, currentUser.getId()));
    }

    // 5. 동아리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClubById(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        clubService.delete(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    // 6. ✅ 가입 신청
    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinRequest(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        clubService.joinRequest(id, currentUser);
        return ResponseEntity.ok().build();
    }

    // 7. ✅ 신청 목록 조회 (master만)
    @GetMapping("/{id}/requests")
    public ResponseEntity<?> getJoinRequests(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clubService.getJoinRequests(id, currentUser));
    }

    // 8. ✅ 승인
    @PostMapping("/{id}/requests/{requestId}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long id,
            @PathVariable Long requestId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        clubService.approve(id, requestId, currentUser);
        return ResponseEntity.ok().build();
    }

    // 9. ✅ 거절
    @PostMapping("/{id}/requests/{requestId}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Long id,
            @PathVariable Long requestId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        clubService.reject(id, requestId, currentUser);
        return ResponseEntity.ok().build();
    }
}