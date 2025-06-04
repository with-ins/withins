package com.withins.core.paging;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageUtils {

    private final JPAQueryFactory queryFactory;


    public <T> Page<T> of(Pageable pageable, JPAQuery<T> query, EntityPathBase<T> entity) {
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        JPAQuery<Long> countResult = queryFactory.select(entity.count())
                .from(entity)
                .where(query.getMetadata().getWhere());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countResult::fetchOne);
    }

    @Nullable
    public BooleanExpression filter(StringExpression compare, String condition, boolean ignoreSpace) {
        if (condition == null || condition.isEmpty()) return null;
        return ignoreSpace
            ? replaceExpression(compare).containsIgnoreCase(condition.replace(" ", ""))
            : compare.containsIgnoreCase(condition);
    }
    @Nullable
    public  <T extends Enum<T>> BooleanExpression filter(EnumPath<T> compare, T condition) {
        return (condition == null) ? null : compare.eq(condition);
    }

    @Nullable
    @SafeVarargs
    public final <T extends Enum<T>> BooleanExpression filter(EnumPath<T> compare, T condition, T... excludes) {
        if (condition == null) return null;
        for (T exclude : excludes) {
            if (exclude.equals(condition)) return null;
        }
        return compare.eq(condition);
    }

    private StringExpression replaceExpression(StringExpression tuple) {
        return Expressions.stringTemplate("replace({0}, ' ', '')", tuple);
    }


}
