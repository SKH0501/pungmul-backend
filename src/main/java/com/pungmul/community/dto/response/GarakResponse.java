package com.pungmul.community.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GarakResponse {
    private Long id;
    private String name;
    private String youtubeUrl;
    private String transitions;
    private String description;
    private LocalDateTime createdAt;
}
