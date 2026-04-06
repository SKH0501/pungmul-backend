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
@Table(name = "clubs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClubType clubType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private User master;

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> members = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime foundedAt;  // 추가!

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum ClubType {
        CENTRAL,    // 중앙 동아리
        DEPARTMENT, // 단과대 학회
        SOCIAL      // 사회패
    }

    // Club.java에 추가
    public void update(String name, String description, String profileImage,
                       Club.ClubType clubType, User master, LocalDateTime foundedAt) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (profileImage != null) this.profileImage = profileImage;
        if (clubType != null) this.clubType = clubType;
        if (master != null) this.master = master;
        if (foundedAt != null) this.foundedAt = foundedAt;
    }
}