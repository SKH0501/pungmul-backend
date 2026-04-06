package com.pungmul.community.dto.request;

import com.pungmul.community.domain.Club;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ClubCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    private String description;

    private String profileImage;

    @NotNull
    private Club.ClubType clubType;

    @NotNull
    private Long masterId;

    private LocalDateTime foundedAt;  // 창단 연도 (선택사항)

}
