package com.sportsecho.member.entity;

import com.sportsecho.cart.entity.Cart;
import com.sportsecho.comment.entity.Comment;
import com.sportsecho.common.time.TimeStamp;
import com.sportsecho.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends TimeStamp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memeber_id")
    private Long id;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Cart> cartList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Order> productList = new ArrayList<>();

    @Builder
    public Member(String memberName, String email, String password, MemberRole role) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member update(String memberName, String password) {
        this.memberName = memberName;
        this.password = password;
        return this;
    }
}
