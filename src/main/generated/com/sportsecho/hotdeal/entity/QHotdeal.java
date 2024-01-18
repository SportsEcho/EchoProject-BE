package com.sportsecho.hotdeal.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHotdeal is a Querydsl query type for Hotdeal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHotdeal extends EntityPathBase<Hotdeal> {

    private static final long serialVersionUID = -623551168L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHotdeal hotdeal = new QHotdeal("hotdeal");

    public final NumberPath<Integer> dealQuantity = createNumber("dealQuantity", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> dueDay = createDateTime("dueDay", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sportsecho.product.entity.QProduct product;

    public final NumberPath<Integer> sale = createNumber("sale", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startDay = createDateTime("startDay", java.time.LocalDateTime.class);

    public QHotdeal(String variable) {
        this(Hotdeal.class, forVariable(variable), INITS);
    }

    public QHotdeal(Path<? extends Hotdeal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHotdeal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHotdeal(PathMetadata metadata, PathInits inits) {
        this(Hotdeal.class, metadata, inits);
    }

    public QHotdeal(Class<? extends Hotdeal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.sportsecho.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

