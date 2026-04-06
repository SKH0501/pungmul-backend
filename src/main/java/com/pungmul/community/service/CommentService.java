package com.pungmul.community.service;

import com.pungmul.community.domain.Comment;
import com.pungmul.community.domain.Review;
import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.CommentCreateRequest;
import com.pungmul.community.dto.request.CommentUpdateRequest;
import com.pungmul.community.dto.response.CommentResponse;
import com.pungmul.community.repository.CommentRepository;
import com.pungmul.community.repository.ReviewRepository;
import com.pungmul.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // 1. 댓글 등록
    public CommentResponse create(CommentCreateRequest request) {
        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 없습니다."));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        Comment comment = Comment.builder()
                        .content(request.getContent())
                        .review(review)
                        .author(user)
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponse.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .reviewId(saved.getReview().getId())
                .authorId(saved.getAuthor().getId())
                .authorName(saved.getAuthor().getName())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 2. 댓글 목록 (후기별)
    // GET /api/comments?reviewId=1
    public List<CommentResponse> getList(Long reviewId) {
        List<Comment> comments = commentRepository.findByReviewId(reviewId);

        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .reviewId(comment.getReview().getId())
                        .authorId(comment.getAuthor().getId())
                        .authorName(comment.getAuthor().getName())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("comment 가 없습니다"));
        comment.update(request.getContent());

        Comment saved = commentRepository.save(comment);

        return CommentResponse.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .reviewId(saved.getReview().getId())
                .authorId(saved.getAuthor().getId())
                .authorName(saved.getAuthor().getName())
                .createdAt(saved.getCreatedAt())
                .build();
    }


    // 4. 댓글 삭제
    public void delete(Long id) {
        Comment comment =  commentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("삭제할 댓글이 없습니다"));
        commentRepository.delete(comment);
    }


}
