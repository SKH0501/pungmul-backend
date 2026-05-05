package com.pungmul.community.service;

import com.pungmul.community.domain.Club;
import com.pungmul.community.domain.Performance;
import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.PerformanceCreateRequest;
import com.pungmul.community.dto.request.PerformanceUpdateRequest;
import com.pungmul.community.dto.response.PerformanceResponse;
import com.pungmul.community.repository.ClubRepository;
import com.pungmul.community.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final ClubRepository clubRepository;

    // ✅ 목록 조회 (페이징 + 검색 + 정렬)
    public Page<PerformanceResponse> getList(
            String keyword,
            Boolean upcoming,
            String sort,
            int page,
            int size) {

        Sort sortOption = "createdAt".equals(sort)
                ? Sort.by(Sort.Direction.DESC, "createdAt")
                : Sort.by(Sort.Direction.ASC, "performedAt");

        Pageable pageable = PageRequest.of(page, size, sortOption);

        return performanceRepository
                .search(keyword, upcoming, LocalDateTime.now(), pageable)
                .map(this::toResponse);
    }

    // ✅ 등록
    @Transactional
    public PerformanceResponse create(PerformanceCreateRequest request, User author) {
        Club club = clubRepository.findById(request.getClubId())
                .orElseThrow(() -> new RuntimeException("동아리를 찾을 수 없어요"));

        Performance performance = Performance.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .performedAt(request.getPerformedAt())
                .performanceType(request.getPerformanceType())
                .posterImage(request.getPosterImage())  // ✅ 추가
                .club(club)
                .author(author)
                .build();

        return toResponse(performanceRepository.save(performance));
    }

    // ✅ 단건 조회
    public PerformanceResponse getOne(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공연이 없습니다."));
        return toResponse(performance);
    }

    // ✅ 수정 (작성자 or ADMIN만)
    @Transactional
    public PerformanceResponse update(Long id, PerformanceUpdateRequest request, User currentUser) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공연을 찾을 수 없습니다."));

        if (!performance.getAuthor().getId().equals(currentUser.getId())
                && currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("작성자 또는 관리자만 수정할 수 있어요");
        }

        performance.update(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getPerformedAt(),
                request.getPerformanceType(),
                request.getPosterImage()  // ✅ 추가
        );

        return toResponse(performanceRepository.save(performance));
    }

    // ✅ 삭제 (작성자 or ADMIN만)
    @Transactional
    public void delete(Long id, User currentUser) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제할 공연이 없습니다"));

        if (!performance.getAuthor().getId().equals(currentUser.getId())
                && currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("작성자 또는 관리자만 삭제할 수 있어요");
        }

        performanceRepository.delete(performance);
    }

    // ✅ 공통 응답 변환
    private PerformanceResponse toResponse(Performance p) {
        String status = LocalDateTime.now().isAfter(p.getPerformedAt())
                ? "COMPLETED" : "UPCOMING";

        return PerformanceResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .location(p.getLocation())
                .posterImage(p.getPosterImage())  // ✅ 추가
                .performedAt(p.getPerformedAt())
                .performanceType(p.getPerformanceType())
                .status(status)  // ✅ 추가
                .clubId(p.getClub().getId())
                .clubName(p.getClub().getName())
                .clubProfileImage(p.getClub().getProfileImage())  // ✅ 추가
                .authorId(p.getAuthor().getId())
                .authorName(p.getAuthor().getName())
                .createdAt(p.getCreatedAt())
                .build();
    }
}