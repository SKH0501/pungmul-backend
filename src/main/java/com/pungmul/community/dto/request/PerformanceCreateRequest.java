package com.pungmul.community.dto.request;

import com.pungmul.community.domain.Club;
import com.pungmul.community.domain.Performance;
import com.pungmul.community.domain.User;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class PerformanceCreateRequest {

    // @NotBlank → String 필드에 (빈값 방지)
    @NotBlank
    private String title; // 클라이언트의 제목 입력
    @NotBlank
    private String description; // 클라이언트의 설명 입력
    @NotBlank
    private String location; // 클라이언트가 위치를 직접 입력

    // @NotNull  → String 외 타입에 (null 방지)
    @NotNull
    private Long userId;  // 임시! 나중에 로그인 구현하면 제거해요
    @NotNull
    private LocalDateTime performedAt; // 클라이언트가 시간을 입력
    @NotNull
    private Performance.PerformanceType performanceType;
    @NotNull
    private Long clubId; // 클라이언트의 club_id
    // User author; // 로그인한 사용자 = 서버가 처리 -> DTO에 불 필요
    // LocalDateTime createdAt; // 서버가 자동 생성 -> DTO에 불 필요
}