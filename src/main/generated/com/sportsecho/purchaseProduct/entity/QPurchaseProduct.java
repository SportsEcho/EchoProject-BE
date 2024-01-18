package com.sportsecho.purchaseProduct.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseProduct is a Querydsl query type for PurchaseProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseProduct extends EntityPathBase<PurchaseProduct> {

    private static final long serialVersionUID = 688407968L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseProduct purchaseProduct = new QPurchaseProduct("purchaseProduct");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sportsecho.product.entity.QProduct product;

    public final NumberPath<Integer> productsQuantity = createNumber("productsQuantity", Integer.class);

    public final com.sportsecho.purchase.entity.QPurchase purchase;

    public QPurchaseProduct(String variable) {
        this(PurchaseProduct.class, forVariable(variable), INITS);
    }

    public QPurchaseProduct(Path<? extends PurchaseProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseProduct(PathMetadata metadata, PathInits inits) {
        this(PurchaseProduct.class, metadata, inits);
    }

    public QPurchaseProduct(Class<? extends PurchaseProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.sportsecho.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.purchase = inits.isInitialized("purchase") ? new com.sportsecho.purchase.entity.QPurchase(forProperty("purchase"), inits.get("purchase")) : null;
    }

}

