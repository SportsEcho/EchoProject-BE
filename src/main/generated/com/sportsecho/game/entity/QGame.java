package com.sportsecho.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGame is a Querydsl query type for Game
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGame extends EntityPathBase<Game> {

    private static final long serialVersionUID = -208616738L;

    public static final QGame game = new QGame("game");

    public final com.sportsecho.common.time.QTimeStamp _super = new com.sportsecho.common.time.QTimeStamp(this);

    public final ListPath<com.sportsecho.comment.entity.Comment, com.sportsecho.comment.entity.QComment> comments = this.<com.sportsecho.comment.entity.Comment, com.sportsecho.comment.entity.QComment>createList("comments", com.sportsecho.comment.entity.Comment.class, com.sportsecho.comment.entity.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> gameDateTime = createDateTime("gameDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath sportType = createString("sportType");

    public final StringPath teamA = createString("teamA");

    public final StringPath teamB = createString("teamB");

    public QGame(String variable) {
        super(Game.class, forVariable(variable));
    }

    public QGame(Path<? extends Game> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGame(PathMetadata metadata) {
        super(Game.class, metadata);
    }

}

