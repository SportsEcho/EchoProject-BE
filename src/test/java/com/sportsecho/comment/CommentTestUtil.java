package com.sportsecho.comment;

import com.sportsecho.comment.entity.Comment;
import com.sportsecho.game.entity.Game;
import com.sportsecho.member.entity.Member;

public class CommentTestUtil {
    public static Comment createTestComment(String content, Game game, Member member) {
        return Comment.builder()
            .content(content)
            .game(game)
            .member(member)
            .build();
    }
}
