package com.sportsecho.game.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "soccer_game")
@NoArgsConstructor
@AllArgsConstructor
public class SoccerScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "game_id", nullable = false)
    private Game game;

    @Column(name = "home_goal", nullable = false)
    private int homeGoal;

    @Column(name = "away_goal", nullable = false)
    private int awayGoal;

    public void updateScore(int homeGoal, int awayGoal) {
        this.homeGoal = homeGoal;
        this.awayGoal = awayGoal;
    }

    public static SoccerScore createSoccerScore(Game game, int homeGoal, int awayGoal) {
        return SoccerScore.builder()
            .game(game)
            .homeGoal(homeGoal)
            .awayGoal(awayGoal)
            .build();
    }
}
