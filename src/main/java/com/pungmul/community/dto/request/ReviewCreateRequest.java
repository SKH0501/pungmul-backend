package com.pungmul.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    @NotBlank
    private String content;
    @NotNull
    private Long performanceId;
    @NotNull
    private Long userId;
}
