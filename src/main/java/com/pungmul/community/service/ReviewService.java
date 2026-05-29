package com.pungmul.community.service;

import com.pungmul.community.domain.Performance;
import com.pungmul.community.domain.Review;
import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.ReviewCreateRequest;
import com.pungmul.community.dto.request.ReviewUpdateRequest;
import com.pungmul.community.dto.response.ReviewResponse;
import com.pungmul.community.repository.PerformanceRepository;
import com.pungmul.community.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PerformanceRepository performanceRepository;

    // 1. 댓글 등록 ✅ User 파라미터 추가
    @Transactional
    public ReviewResponse create(ReviewCreateRequest request, User author) {
        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new RuntimeException("공연이 없습니다."));

        Review review = Review.builder()
                .content(request.getContent())
                .performance(performance)
                .author(author)
                .build();

        return toResponse(reviewRepository.save(review));
    }

    // 2. 댓글 목록 (공연별)
    public List<ReviewResponse> getList(Long performanceId) {
        return reviewRepository.findByPerformanceId(performanceId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 3. 댓글 단건 조회
    public ReviewResponse getOne(Long id) {
        return toResponse(reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다.")));
    }

    // 4. 댓글 수정 ✅ 권한 체크 추가
    @Transactional
    public ReviewResponse update(Long id, ReviewUpdateRequest request, User currentUser) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        if (!review.getAuthor().getId().equals(currentUser.getId())
                && currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("작성자 또는 관리자만 수정할 수 있어요");
        }

        review.update(request.getContent());
        return toResponse(reviewRepository.save(review));
    }

    // 5. 댓글 삭제 ✅ 권한 체크 추가
    @Transactional
    public void delete(Long id, User currentUser) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        if (!review.getAuthor().getId().equals(currentUser.getId())
                && currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("작성자 또는 관리자만 삭제할 수 있어요");
        }

        reviewRepository.delete(review);
    }

    // ✅ 공통 응답 변환
    private ReviewResponse toResponse(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .content(r.getContent())
                .performanceId(r.getPerformance().getId())
                .performanceTitle(r.getPerformance().getTitle())
                .clubName(r.getPerformance().getClub().getName())
                .authorId(r.getAuthor().getId())
                .authorName(r.getAuthor().getName())
                .authorProfileImage(r.getAuthor().getProfileImage() != null
                        ? r.getAuthor().getProfileImage() : "")
                .commentCount(r.getComments().size())
                .likeCount(r.getLikes().size())
                .createdAt(r.getCreatedAt())
                .build();
    }
}