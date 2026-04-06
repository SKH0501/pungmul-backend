package com.pungmul.community.service;

import com.pungmul.community.domain.Club;
import com.pungmul.community.domain.Performance;
import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.PerformanceCreateRequest;
import com.pungmul.community.dto.request.PerformanceUpdateRequest;
import com.pungmul.community.dto.response.PerformanceResponse;
import com.pungmul.community.repository.ClubRepository;
import com.pungmul.community.repository.PerformanceRepository;
import com.pungmul.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    public PerformanceResponse create(PerformanceCreateRequest request) {
        // Club 이 있는지 확인하기.
        Club club = clubRepository.findById(request.getClubId())
                .orElseThrow(() -> new RuntimeException("동아리를 찾을 수 없어요"));
        // 유저 조회 임시 추가.
        User author = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없어요"));


        // Request -> Entity 변환
        Performance performance = Performance.builder()
                .title(request.getTitle())          // Request에서 꺼내서
                .description(request.getDescription())
                .location(request.getLocation())
                .performedAt(request.getPerformedAt())
                .performanceType(request.getPerformanceType())
                .club(club)                          // 위에서 조회한 Club 객체
                .author(author)
                .build();
        // DB 저장
        Performance saved = performanceRepository.save(performance);

        // Entity -> Response 변환
        return PerformanceResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .location(saved.getLocation())
                .performedAt(saved.getPerformedAt())
                .performanceType(saved.getPerformanceType())
                .clubId(saved.getClub().getId())
                .clubName(saved.getClub().getName())
                .createdAt(saved.getCreatedAt())
                .build();

    }

    public List<PerformanceResponse> getList(){
            List<Performance> performances = performanceRepository.findAll();
            return performances.stream()
                    .map(performance -> PerformanceResponse.builder()
                            .id(performance.getId())
                            .title(performance.getTitle())
                            .location(performance.getLocation())
                            .performedAt(performance.getPerformedAt())
                            .performanceType(performance.getPerformanceType())
                            .clubId(performance.getClub().getId())
                            .clubName(performance.getClub().getName())
                            .createdAt(performance.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());
    }

    public PerformanceResponse getOne(Long id) {
        // 힌트: findById + orElseThrow 써요
        Performance performance = performanceRepository.findById(id).
                orElseThrow(() -> new RuntimeException("공연이 없습니다."));
        // 그 다음 Entity → ResponseDTO 변환
        return PerformanceResponse.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .description(performance.getDescription())
                .location(performance.getLocation())
                .performedAt(performance.getPerformedAt())
                .performanceType(performance.getPerformanceType())
                .clubId(performance.getClub().getId())
                .clubName(performance.getClub().getName())
                .createdAt(performance.getCreatedAt())
                .build();

    }

    public void delete(Long id){
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("삭제할 공연이 없습니다"));
        performanceRepository.delete(performance);
    }

    public PerformanceResponse update (Long id ,PerformanceUpdateRequest request) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공연을 찾을 수 없습니다."));

        performance.update(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getPerformedAt(),
                request.getPerformanceType()
        );

        Performance saved = performanceRepository.save(performance);

        return PerformanceResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .location(saved.getLocation())
                .performedAt(saved.getPerformedAt())
                .performanceType(saved.getPerformanceType())
                .clubId(saved.getClub().getId())
                .clubName(saved.getClub().getName())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
