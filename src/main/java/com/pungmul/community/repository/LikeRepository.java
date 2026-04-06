package com.pungmul.community.repository;

import com.pungmul.community.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndReviewId(Long userId, Long reviewId);
    int countByReviewId(Long reviewId);
}