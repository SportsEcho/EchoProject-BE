package com.sportsecho.game.repository;

import com.sportsecho.game.entity.Game;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    boolean existsByDate(LocalDateTime date);
}
