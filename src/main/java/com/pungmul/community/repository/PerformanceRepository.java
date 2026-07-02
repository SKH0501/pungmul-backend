package com.pungmul.community.repository;

import com.pungmul.community.domain.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    List<Performance> findByClubId(Long clubId);

    // ✅ 검색 + 페이징
    @Query("SELECT p FROM Performance p WHERE " +
            "(:keyword IS NULL OR p.title LIKE %:keyword% OR p.location LIKE %:keyword% OR p.club.name LIKE %:keyword%) AND " +
            "(:upcoming IS NULL OR (:upcoming = true AND p.performedAt > :now) OR (:upcoming = false AND p.performedAt <= :now))")
    Page<Performance> search(
            @Param("keyword") String keyword,
            @Param("upcoming") Boolean upcoming,
            @Param("now") LocalDateTime now,
            Pageable pageable
    );
}