package com.pungmul.community.service;

import com.pungmul.community.domain.Performance;
import com.pungmul.community.domain.Review;
import com.pungmul.community.domain.User;
import com.pungmul.community.dto.request.ReviewCreateRequest;
import com.pungmul.community.dto.request.ReviewUpdateRequest;
import com.pungmul.community.dto.response.PerformanceResponse;
import com.pungmul.community.dto.response.ReviewResponse;
import com.pungmul.community.repository.PerformanceRepository;
import com.pungmul.community.repository.ReviewRepository;
import com.pungmul.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;

    // 1. 후기 등록
    public ReviewResponse create(ReviewCreateRequest request) {
        // Performance 조회
        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(()-> new RuntimeException("공연이 없습니다."));
        // User 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("유저가 없습니다."));

        // Request → Entity 변환
        Review review = Review.builder()
                .content(request.getContent())      // content는 request에서
                .performance(performance)            // 위에서 조회한 performance 객체
                .author(user)                        // 위에서 조회한 user 객체
                .build();
        // DB 저장
        Review saved =  reviewRepository.save(review);
        // Entity → Response 변환
        return ReviewResponse.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .performanceId(saved.getPerformance().getId())
                .performanceTitle(saved.getPerformance().getTitle())
                .clubName(saved.getPerformance().getClub().getName())
                .authorId(saved.getAuthor().getId())
                .authorName(saved.getAuthor().getName())
                .commentCount(saved.getComments().size())
                .likeCount(saved.getLikes().size())
                .createdAt(saved.getCreatedAt())
                .build();

    }

    // 2. 후기 목록 (공연별)
    public List<ReviewResponse> getList(Long performanceId) {
        // 1. performanceId로 후기 목록 조회
        List<Review> reviews = reviewRepository.findByPerformanceId(performanceId);
        // 2. stream으로 ReviewResponse 변환
        // 힌트: Performance getList의 stream 패턴이랑 똑같아요!
        return reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .content(review.getContent())
                        .performanceId(review.getPerformance().getId())
                        .performanceTitle(review.getPerformance().getTitle())
                        .clubName(review.getPerformance().getClub().getName())
                        .authorId(review.getAuthor().getId())
                        .authorName(review.getAuthor().getName())
                        .commentCount(review.getComments().size())
                        .likeCount(review.getLikes().size())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

    }

    // 3. 후기 단건 조회
    public ReviewResponse getOne(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 review 가 존재하지 않습니다."));

        return ReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .performanceId(review.getPerformance().getId())
                .performanceTitle(review.getPerformance().getTitle())
                .clubName(review.getPerformance().getClub().getName())
                .authorId(review.getAuthor().getId())
                .authorName(review.getAuthor().getName())
                .commentCount(review.getComments().size())
                .likeCount(review.getLikes().size())
                .createdAt(review.getCreatedAt())
                .build();
    }

    // 4. 후기 수정
    public ReviewResponse update(Long id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 review 가 존재하지 않습니다."));
        review.update(
                request.getContent()
        );

        Review saved =  reviewRepository.save(review);

        return ReviewResponse.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .performanceId(saved.getPerformance().getId())
                .performanceTitle(saved.getPerformance().getTitle())
                .clubName(saved.getPerformance().getClub().getName())
                .authorId(saved.getAuthor().getId())
                .authorName(saved.getAuthor().getName())
                .commentCount(saved.getComments().size())
                .likeCount(saved.getLikes().size())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 5. 후기 삭제
    public void delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" 삭제할 공연이 안보입니다 "));
        reviewRepository.delete(review);
    }

}
