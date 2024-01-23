package com.sportsecho.game.repository;

import com.sportsecho.game.entity.SoccerScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoccerScoreRepository extends JpaRepository<SoccerScore, Long> {

}
