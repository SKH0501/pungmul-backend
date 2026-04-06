package com.pungmul.community.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "performances")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime performedAt;  // 공연 날짜/시간

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerformanceType performanceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Builder.Default
    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum PerformanceType {
        REGULAR,    // 정기공연
        FESTIVAL,   // 축제
        EXCHANGE,   // 교류공연
        BUSKING     // 버스킹
    }
    // Performance.java에 추가
    public void update(String title, String description,
                       String location, LocalDateTime performedAt,
                       Performance.PerformanceType performanceType) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (location != null) this.location = location;
        if (performedAt != null) this.performedAt = performedAt;
        if (performanceType != null) this.performanceType = performanceType;
    }
}