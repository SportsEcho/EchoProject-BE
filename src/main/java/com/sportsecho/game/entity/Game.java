package com.sportsecho.game.entity;

import com.sportsecho.comment.entity.Comment;
import com.sportsecho.common.time.TimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "game")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Game extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sportType; // 축구, 농구, 야구 구분
    private String teamA;
    private String teamB;
    private LocalDateTime gameDateTime; // 경기 일시
    private String location; // 경기 장소

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

}
