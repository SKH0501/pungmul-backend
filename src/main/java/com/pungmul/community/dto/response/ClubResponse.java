package com.pungmul.community.dto.response;

import com.pungmul.community.domain.Club;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClubResponse {
    private Long id;
    private String name;
    private String location;
    private String description;
    private String profileImage;
    private Club.ClubType clubType;

    private Long masterId;        // 대표 id
    private String masterName;    // 대표 이름

    private int memberCount;      // 멤버 수

    private LocalDateTime foundedAt;   // 창단일
    private LocalDateTime createdAt;   // 서버 등록일

}
