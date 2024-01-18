package com.sportsecho.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1370479232L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final StringPath content = createString("content");

    public final com.sportsecho.hotdeal.entity.QHotdeal hotdeal;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final ListPath<com.sportsecho.memberProduct.entity.MemberProduct, com.sportsecho.memberProduct.entity.QMemberProduct> memberProductList = this.<com.sportsecho.memberProduct.entity.MemberProduct, com.sportsecho.memberProduct.entity.QMemberProduct>createList("memberProductList", com.sportsecho.memberProduct.entity.MemberProduct.class, com.sportsecho.memberProduct.entity.QMemberProduct.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<com.sportsecho.purchaseProduct.entity.PurchaseProduct, com.sportsecho.purchaseProduct.entity.QPurchaseProduct> PurchaseProductList = this.<com.sportsecho.purchaseProduct.entity.PurchaseProduct, com.sportsecho.purchaseProduct.entity.QPurchaseProduct>createList("PurchaseProductList", com.sportsecho.purchaseProduct.entity.PurchaseProduct.class, com.sportsecho.purchaseProduct.entity.QPurchaseProduct.class, PathInits.DIRECT2);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final StringPath title = createString("title");

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hotdeal = inits.isInitialized("hotdeal") ? new com.sportsecho.hotdeal.entity.QHotdeal(forProperty("hotdeal"), inits.get("hotdeal")) : null;
    }

}

