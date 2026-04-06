package com.pungmul.community.dto.request;

import com.pungmul.community.domain.Club;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ClubUpdateRequest {
    private String name;
    private String description;
    private String profileImage;
    private Club.ClubType clubType;
    private Long masterId;    // 대표 변경 가능!
    private LocalDateTime foundedAt;
}
