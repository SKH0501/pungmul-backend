package com.pungmul.community.repository;

import com.pungmul.community.domain.ClubJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClubJoinRepository extends JpaRepository<ClubJoin, Long> {
    List<ClubJoin> findByClubIdAndStatus(Long clubId, ClubJoin.JoinStatus status);
    Optional<ClubJoin> findByUserIdAndClubId(Long userId, Long clubId);
    boolean existsByUserIdAndClubIdAndStatus(Long userId, Long clubId, ClubJoin.JoinStatus status);
}