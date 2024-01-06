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

    private String content;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "member_name")
    private String memberName; // 댓글 작성자 이름
}