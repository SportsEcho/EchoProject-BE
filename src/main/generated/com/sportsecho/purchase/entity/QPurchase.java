package com.sportsecho.purchase.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchase is a Querydsl query type for Purchase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchase extends EntityPathBase<Purchase> {

    private static final long serialVersionUID = -1121252740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchase purchase = new QPurchase("purchase");

    public final StringPath address = createString("address");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sportsecho.member.entity.QMember member;

    public final StringPath phone = createString("phone");

    public final ListPath<com.sportsecho.purchaseProduct.entity.PurchaseProduct, com.sportsecho.purchaseProduct.entity.QPurchaseProduct> purchaseProductList = this.<com.sportsecho.purchaseProduct.entity.PurchaseProduct, com.sportsecho.purchaseProduct.entity.QPurchaseProduct>createList("purchaseProductList", com.sportsecho.purchaseProduct.entity.PurchaseProduct.class, com.sportsecho.purchaseProduct.entity.QPurchaseProduct.class, PathInits.DIRECT2);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public QPurchase(String variable) {
        this(Purchase.class, forVariable(variable), INITS);
    }

    public QPurchase(Path<? extends Purchase> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchase(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchase(PathMetadata metadata, PathInits inits) {
        this(Purchase.class, metadata, inits);
    }

    public QPurchase(Class<? extends Purchase> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.sportsecho.member.entity.QMember(forProperty("member")) : null;
    }

}

