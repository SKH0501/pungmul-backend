package com.pungmul.community.service;

import com.pungmul.community.domain.*;
import com.pungmul.community.dto.request.ClubCreateRequest;
import com.pungmul.community.dto.request.ClubUpdateRequest;
import com.pungmul.community.dto.response.ClubResponse;
import com.pungmul.community.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubJoinRepository clubJoinRepository;  // ✅ 이름 변경


    // 1. 동아리 등록 ✅ User 파라미터 추가
    @Transactional
    public ClubResponse create(ClubCreateRequest request, User master) {
        clubRepository.findByNameAndLocation(request.getName(), request.getLocation())
                .ifPresent(c -> { throw new RuntimeException("이미 존재하는 동아리예요!"); });

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

        // ✅ 만든 사람 자동으로 ClubMember(ADMIN)으로 추가
        ClubMember clubMember = ClubMember.builder()
                .user(master)
                .club(saved)
                .memberRole(ClubMember.MemberRole.ADMIN)
                .build();
        clubMemberRepository.save(clubMember);

        return toResponse(saved);
    }

    // 2. 동아리 목록
    public List<ClubResponse> getList() {
        return clubRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 3. 동아리 단건 조회
    public ClubResponse getOne(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다"));
        return toResponse(club);
    }

    // 4. 동아리 수정
    @Transactional
    public ClubResponse update(Long id, ClubUpdateRequest request, Long currentUserId) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다."));

        if (!club.getMaster().getId().equals(currentUserId)) {
            throw new RuntimeException("동아리 대표자만 수정할 수 있어요");
        }

        User master = null;
        if (request.getMasterId() != null) {
            master = userRepository.findById(request.getMasterId())
                    .orElseThrow(() -> new RuntimeException("대표자가 없습니다"));
        }

        club.update(request.getName(), request.getDescription(),
                request.getProfileImage(), request.getClubType(),
                master, request.getFoundedAt());

        return toResponse(clubRepository.save(club));
    }

    // 5. 동아리 삭제
    @Transactional
    public void delete(Long id, Long currentUserId) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("동아리가 존재하지 않습니다"));

        if (!club.getMaster().getId().equals(currentUserId)) {
            throw new RuntimeException("동아리 대표자만 삭제할 수 있어요");
        }

        clubRepository.delete(club);
    }

    // 6. 가입 신청
    @Transactional
    public void joinRequest(Long clubId, User user) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다"));

        if (clubMemberRepository.existsByUserIdAndClubId(user.getId(), clubId)) {
            throw new RuntimeException("이미 가입된 동아리예요");
        }

        if (clubJoinRepository.existsByUserIdAndClubIdAndStatus(
                user.getId(), clubId, ClubJoin.JoinStatus.PENDING)) {
            throw new RuntimeException("이미 가입 신청 중이에요");
        }

        ClubJoin join = ClubJoin.builder()
                .user(user)
                .club(club)
                .build();
        clubJoinRepository.save(join);
    }

    // 7. 신청 목록 조회
    public List<?> getJoinRequests(Long clubId, User currentUser) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다"));

        if (!club.getMaster().getId().equals(currentUser.getId())) {
            throw new RuntimeException("대표자만 신청 목록을 볼 수 있어요");
        }

        return clubJoinRepository
                .findByClubIdAndStatus(clubId, ClubJoin.JoinStatus.PENDING)
                .stream()
                .map(r -> java.util.Map.of(
                        "requestId", r.getId(),
                        "userId", r.getUser().getId(),
                        "userName", r.getUser().getName(),
                        "userSchool", r.getUser().getSchool() != null ? r.getUser().getSchool() : "",
                        "requestedAt", r.getRequestedAt()
                ))
                .collect(Collectors.toList());
    }

    // 8. 승인
    @Transactional
    public void approve(Long clubId, Long requestId, User currentUser) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다"));

        if (!club.getMaster().getId().equals(currentUser.getId())) {
            throw new RuntimeException("대표자만 승인할 수 있어요");
        }

        ClubJoin join = clubJoinRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청이 없습니다"));

        join.approve();
        clubJoinRepository.save(join);

        ClubMember member = ClubMember.builder()
                .user(join.getUser())
                .club(club)
                .memberRole(ClubMember.MemberRole.MEMBER)
                .build();
        clubMemberRepository.save(member);
    }

    // 9. 거절
    @Transactional
    public void reject(Long clubId, Long requestId, User currentUser) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("동아리가 없습니다"));

        if (!club.getMaster().getId().equals(currentUser.getId())) {
            throw new RuntimeException("대표자만 거절할 수 있어요");
        }

        ClubJoin join = clubJoinRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청이 없습니다"));

        join.reject();
        clubJoinRepository.save(join);
    }


    // ✅ 공통 응답 변환
    private ClubResponse toResponse(Club club) {
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
}