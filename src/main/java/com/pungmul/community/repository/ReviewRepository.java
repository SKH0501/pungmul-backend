package com.pungmul.community.repository;

import com.pungmul.community.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPerformanceId(Long performanceId);
    List<Review> findByAuthorId(Long authorId);
}