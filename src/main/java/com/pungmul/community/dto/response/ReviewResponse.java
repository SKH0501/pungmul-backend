package com.pungmul.community.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Long id;                    // 후기 id
    private String content;             // 후기 내용
    private Long performanceId;    // 공연 id
    private String performanceTitle;    // 공연 제목
    private String clubName;            // 동아리 이름
    private Long authorId;              // 작성자 id
    private String authorName;          // 작성자 이름
    private int commentCount;           // 댓글 수
    private int likeCount;              // 좋아요 수
    private LocalDateTime createdAt;    // 작성일

}
