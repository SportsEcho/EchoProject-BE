package com.sportsecho.game.repository;

import com.sportsecho.game.entity.Game;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE DATE_FORMAT(g.date, '%Y-%m-%d') = :date")
    List<Game> findByDate(@Param("date") String date);

    Optional<Game> findByDateAndHomeTeamNameAndAwayTeamName(LocalDateTime date, String homeTeamName, String awayTeamName);
}
