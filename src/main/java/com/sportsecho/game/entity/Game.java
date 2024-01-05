package com.sportsecho.game.entity;

import com.sportsecho.comment.entity.Comment;
import com.sportsecho.common.time.TimeStamp;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "matchs")
@NoArgsConstructor
public class Game extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sportType; // 축구, 농구, 야구 구분
    private String teamA;
    private String teamB;
    private LocalDateTime matchDateTime; // 경기 일시
    private String location; // 경기 장소

    @OneToMany(mappedBy = "match")
    private List<Comment> comments = new ArrayList<>();

}
