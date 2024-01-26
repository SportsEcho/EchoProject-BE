package com.sportsecho.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.entity.QProduct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final EntityManager em;

    @Override
    public List<Product> findAllByKeywordWithPage(Pageable pageable, String keyword) {

        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(product.title.like("%" + keyword + "%"));

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query
                .selectFrom(product)
                .where(builder)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
