package com.pungmul.community.repository;

import com.pungmul.community.domain.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    List<ClubMember> findByClubId(Long clubId);
    List<ClubMember> findByUserId(Long userId);
    Optional<ClubMember> findByUserIdAndClubId(Long userId, Long clubId);
}