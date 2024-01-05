package com.sportsecho.game.entity;

import com.sportsecho.comment.entity.Comment;
import com.sportsecho.common.time.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "match")
@NoArgsConstructor
public class Game extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamA;
    private String teamB;
    private LocalDateTime matchDateTime; // 경기 일시
    private String location; // 경기 장소

    @OneToMany(mappedBy = "match")
    private List<Comment> comments = new ArrayList<>();
}
