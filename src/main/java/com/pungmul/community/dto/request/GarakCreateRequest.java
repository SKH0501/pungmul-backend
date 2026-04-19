package com.pungmul.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GarakCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String youtubeUrl;

    private String transitions;
    private String description;

}
