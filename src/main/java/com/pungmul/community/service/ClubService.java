package com.pungmul.community.service;

import com.pungmul.community.domain.Club;
import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.ClubCreateRequest;
import com.pungmul.community.dto.request.ClubUpdateRequest;
import com.pungmul.community.dto.response.ClubResponse;
import com.pungmul.community.repository.ClubRepository;
import com.pungmul.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {

    // 어떤 Repository 필요할까요?
    // 힌트: Club, User
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    // 1. 동아리 등록
    public ClubResponse create(ClubCreateRequest request) {
        clubRepository.findByNameAndLocation(request.getName(), request.getLocation())
                .ifPresent(c -> { throw new RuntimeException("이미 존재하는 동아리예요!"); });
        User master = userRepository.findById(request.getMasterId())
                .orElseThrow(()->new RuntimeException("유저가 존재하지 않습니다"));


        Club club = Club.builder()
                .name(request.getName())
                .location(request.getLocation())
                .description(request.getDescription())
                .profileImage(request.getProfileImage())
                .clubType(request.getClubType())
                .master(master)
                .foundedAt(request.getFoundedAt())
                .build();

        Club saved = clubRepository.save(club);

        return ClubResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .location(saved.getLocation())
                .description(saved.getDescription())
                .profileImage(saved.getProfileImage())
                .clubType(saved.getClubType())
                .masterId(saved.getMaster().getId())
                .masterName(saved.getMaster().getName())  // masterName 추가!
                .memberCount(saved.getMembers().size())   // memberCount 추가!
                .foundedAt(saved.getFoundedAt())
                .createdAt(saved.getCreatedAt())          // createdAt 추가!
                .build();

    }

    // 2. 동아리 목록
    public List<ClubResponse> getList() {
        List<Club> clubs = clubRepository.findAll();
        return clubs.stream()
                .map(club ->ClubResponse.builder()
                        .id(club.getId())
                        .name(club.getName())
                        .location(club.getLocation())
                        .description(club.getDescription())
                        .profileImage(club.getProfileImage())
                        .clubType(club.getClubType())
                        .masterId(club.getMaster().getId())
                        .masterName(club.getMaster().getName())
                        .memberCount(club.getMembers().size())
                        .foundedAt(club.getFoundedAt())
                        .createdAt(club.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 3. 동아리 단건 조회
    public ClubResponse getOne(Long id) {
        Club club =  clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다"));
        return ClubResponse.builder()
                .id(club.getId())
                .name(club.getName())
                .location(club.getLocation())
                .description(club.getDescription())
                .profileImage(club.getProfileImage())
                .clubType(club.getClubType())
                .masterId(club.getMaster().getId())
                .masterName(club.getMaster().getName())
                .memberCount(club.getMembers().size())
                .foundedAt(club.getFoundedAt())
                .createdAt(club.getCreatedAt())
                .build();
    }

    // 4. 동아리 수정
    public ClubResponse update(Long id, ClubUpdateRequest request, Long currentUserId) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다."));

        // 권한 체크!
        if (!club.getMaster().getId().equals(currentUserId)) {
            throw new RuntimeException("동아리 대표자만 수정할 수 있어요");
        }

        User master = null;
        if (request.getMasterId() != null) {
            master = userRepository.findById(request.getMasterId())
                    .orElseThrow(() -> new RuntimeException("대표자가 없습니다"));
        }

        club.update(
                request.getName(),
                request.getDescription(),
                request.getProfileImage(),
                request.getClubType(),
                master,
                request.getFoundedAt()
        );
        Club saved = clubRepository.save(club);

        return ClubResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .location(saved.getLocation())
                .description(saved.getDescription())
                .profileImage(saved.getProfileImage())
                .clubType(saved.getClubType())
                .masterId(saved.getMaster().getId())
                .masterName(saved.getMaster().getName())
                .memberCount(saved.getMembers().size())
                .foundedAt(saved.getFoundedAt())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 5. 동아리 삭제
    public void delete(Long id, Long currentUserId) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동아리가 존재하지 않습니다"));

        // 권한 체크!
        if (!club.getMaster().getId().equals(currentUserId)) {
            throw new RuntimeException("동아리 대표자만 삭제할 수 있어요");
        }

        clubRepository.delete(club);
    }
}