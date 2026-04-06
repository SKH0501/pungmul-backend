package com.pungmul.community.repository;

import com.pungmul.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewId(Long reviewId);
    List<Comment> findByAuthorId(Long authorId);  // 혹시 추가했다면 이렇게

}