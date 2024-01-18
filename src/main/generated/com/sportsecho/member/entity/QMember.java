package com.sportsecho.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1548301330L;

    public static final QMember member = new QMember("member1");

    public final ListPath<com.sportsecho.comment.entity.Comment, com.sportsecho.comment.entity.QComment> commentList = this.<com.sportsecho.comment.entity.Comment, com.sportsecho.comment.entity.QComment>createList("commentList", com.sportsecho.comment.entity.Comment.class, com.sportsecho.comment.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memberName = createString("memberName");

    public final ListPath<com.sportsecho.memberProduct.entity.MemberProduct, com.sportsecho.memberProduct.entity.QMemberProduct> memberProductList = this.<com.sportsecho.memberProduct.entity.MemberProduct, com.sportsecho.memberProduct.entity.QMemberProduct>createList("memberProductList", com.sportsecho.memberProduct.entity.MemberProduct.class, com.sportsecho.memberProduct.entity.QMemberProduct.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<com.sportsecho.purchase.entity.Purchase, com.sportsecho.purchase.entity.QPurchase> productList = this.<com.sportsecho.purchase.entity.Purchase, com.sportsecho.purchase.entity.QPurchase>createList("productList", com.sportsecho.purchase.entity.Purchase.class, com.sportsecho.purchase.entity.QPurchase.class, PathInits.DIRECT2);

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public final NumberPath<Long> socialId = createNumber("socialId", Long.class);

    public final EnumPath<com.sportsecho.common.oauth.SocialType> socialType = createEnum("socialType", com.sportsecho.common.oauth.SocialType.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

