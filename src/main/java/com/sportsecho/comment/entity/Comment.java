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
    @JoinColumn(name = "match_id")
    private Game match;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 댓글을 남긴 멤버

}
