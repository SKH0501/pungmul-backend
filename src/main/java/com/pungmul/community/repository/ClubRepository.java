package com.pungmul.community.repository;

import com.pungmul.community.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByLocation(String university);
    List<Club> findByClubType(Club.ClubType clubType);
    Optional<Club> findByNameAndLocation(String name, String location);

}