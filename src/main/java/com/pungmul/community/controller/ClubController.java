package com.pungmul.community.controller;


import com.pungmul.community.domain.Club;
import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.ClubCreateRequest;
import com.pungmul.community.dto.request.ClubUpdateRequest;
import com.pungmul.community.dto.response.ClubResponse;
import com.pungmul.community.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    // 1. POST /api/clubs
    @PostMapping()
    public ResponseEntity<ClubResponse> addClub(@RequestBody ClubCreateRequest request){
        ClubResponse clubResponse = clubService.create(request);
        return ResponseEntity.ok().body(clubResponse);
    }
    // 2. GET /api/clubs
    @GetMapping()
    public ResponseEntity<List<ClubResponse>> getList(){
        List<ClubResponse> clubResponses= clubService.getList();
        return ResponseEntity.ok().body(clubResponses);
    }
    // 3. GET /api/clubs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ClubResponse> getClubById(@PathVariable Long id){
        ClubResponse clubResponse = clubService.getOne(id);
        return ResponseEntity.ok().body(clubResponse);
    }
    // 4. PATCH /api/clubs/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<ClubResponse> updateClub(
            @PathVariable Long id,
            @RequestBody ClubUpdateRequest request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        ClubResponse clubResponse = clubService.update(id, request, currentUser.getId());
        return ResponseEntity.ok().body(clubResponse);
    }

    // 5. DELETE /api/clubs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClubById(
            @PathVariable Long id,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        clubService.delete(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

}
