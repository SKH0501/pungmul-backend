package com.pungmul.community.service;

import com.pungmul.community.domain.Garak;
import com.pungmul.community.dto.request.GarakCreateRequest;
import com.pungmul.community.dto.response.GarakResponse;
import com.pungmul.community.repository.GarakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GarakService {

    private final GarakRepository garakRepository;

    // 가락 등록
    public GarakResponse create(GarakCreateRequest request) {
        Garak garak = Garak.builder()
                .name(request.getName())
                .youtubeUrl(request.getYoutubeUrl())
                .transitions(request.getTransitions())
                .description(request.getDescription())
                .build();

        Garak saved = garakRepository.save(garak);
        return toResponse(saved);
    }

    // 가락 목록
    public List<GarakResponse> getList() {
        return garakRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 가락 단건 조회
    public GarakResponse getOne(Long id) {
        Garak garak = garakRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("가락을 찾을 수 없어요"));
        return toResponse(garak);
    }

    // Entity → Response 변환
    private GarakResponse toResponse(Garak garak) {
        return GarakResponse.builder()
                .id(garak.getId())
                .name(garak.getName())
                .youtubeUrl(garak.getYoutubeUrl())
                .transitions(garak.getTransitions())
                .description(garak.getDescription())
                .createdAt(garak.getCreatedAt())
                .build();
    }
}