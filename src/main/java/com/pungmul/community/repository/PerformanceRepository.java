package com.pungmul.community.repository;

import com.pungmul.community.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    List<Performance> findByClubId(Long clubId);
    List<Performance> findByOrderByPerformedAtDesc();
}