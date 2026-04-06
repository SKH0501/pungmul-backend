package com.pungmul.community.dto.response;

import com.pungmul.community.domain.Club;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private Long reviewId;
    private Long authorId;      // 나중에 프로필 페이지 이동할 때 쓸 id
    private String authorName;  // 화면에 보여줄 이름
    private LocalDateTime createdAt;

}
