package com.withins.core.news.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.withins.core.news.dto.NewsCondition;
import com.withins.core.news.entity.News;
import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.news.enums.NewsType;
import com.withins.core.paging.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.withins.core.news.entity.QNews.*;
import static com.withins.core.welfarecenter.entity.QWelfareCenter.*;

@Repository
@RequiredArgsConstructor
public class NewsQDSLRepository {

    private final JPAQueryFactory query;
    private final PageUtils pageUtils;

    public Page<News> readAllNewsWithPage(Pageable pageable, NewsCondition condition) {

        BooleanExpression[] conditions = generateCondition(condition);

        JPAQuery<News> jpaQuery = query.selectFrom(news)
                .join(news.welfareCenter, welfareCenter)
                .where(conditions)
                .orderBy(news.newsCreatedAt.desc());

        return pageUtils.of(pageable, jpaQuery, news);
    }

    private BooleanExpression[] generateCondition(NewsCondition condition) {
        if (condition == null) return null;
        BooleanExpression region = pageUtils.filter(news.welfareCenter.region, condition.getRegion(), KoreanRegion.ALL);
        BooleanExpression newsType = pageUtils.filter(news.type, condition.getType(), NewsType.ALL);
        BooleanExpression word = pageUtils.filter(news.title, condition.getWord(), true);
        return new BooleanExpression[] {word, newsType, region};
    }

}
