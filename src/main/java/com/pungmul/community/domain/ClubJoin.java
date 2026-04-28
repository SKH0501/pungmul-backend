package com.pungmul.community.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "club_joins")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private JoinStatus status = JoinStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @PrePersist
    public void prePersist() {
        this.requestedAt = LocalDateTime.now();
    }

    public void approve() { this.status = JoinStatus.APPROVED; }
    public void reject() { this.status = JoinStatus.REJECTED; }

    public enum JoinStatus {
        PENDING, APPROVED, REJECTED
    }
}