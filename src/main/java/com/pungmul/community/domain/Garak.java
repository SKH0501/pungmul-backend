package com.pungmul.community.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "garaks")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;           // "채굿"

    @Column(nullable = false)
    private String youtubeUrl;     // 유튜브 URL

    @Column(columnDefinition = "TEXT")
    private String transitions;    // JSON 형태로 전환 시점 저장

    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void update(String transitions) {
        if (transitions != null) this.transitions = transitions;
    }

}
