package com.pungmul.community.dto.request;

import com.pungmul.community.domain.Performance;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PerformanceUpdateRequest {
    private String title;           // @NotBlank 없음!
    private String description;
    private String location;
    private LocalDateTime performedAt;
    private Performance.PerformanceType performanceType;
}
