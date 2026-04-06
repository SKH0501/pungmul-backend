package com.pungmul.community.dto.request;


import com.pungmul.community.domain.Review;
import com.pungmul.community.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    // 클라이언트가 직접 입력하는것만 DTO 에 넣어야한다.
    @NotBlank
    private String content;    // 댓글 내용

    @NotNull
    private Long reviewId;     // 어떤 후기의 댓글인지

    @NotNull
    private Long userId;       // 임시 (나중에 OAuth로 대체)

}
