package com.sportsecho.comment.entity;

import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.game.entity.Game;
import com.sportsecho.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class Comment extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(Member member, Game game, String username, String content) {
        this.member = member;
        this.game = game;
        this.username = username;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

}