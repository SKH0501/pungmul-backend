package com.pungmul.community.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import com.pungmul.community.domain.Performance;

@Getter
@Builder
public class PerformanceResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime performedAt;
    private Performance.PerformanceType performanceType;

    // Club 객체 대신 필요한 것만!
    private Long clubId;
    private String clubName;

    // User 객체 대신 필요한 것만!
    private Long authorId;
    private String authorName;

    private LocalDateTime createdAt;
}