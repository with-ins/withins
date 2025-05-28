package com.withins.core.paging;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class PageUtils {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 30;

    public static Pageable createByDefault() {
        return of(DEFAULT_PAGE, DEFAULT_SIZE);
    }

    public static Pageable of(int page, int sizePerPage) {
        int validatedPage = Math.max(page, DEFAULT_PAGE);
        int validatedSize = (sizePerPage > MAX_SIZE ? DEFAULT_SIZE : sizePerPage);
        return org.springframework.data.domain.PageRequest.of(validatedPage, validatedSize);
    }

    public static <T> Page<T> toPage(JPAQuery<T> contentQuery, JPAQuery<Long> countQuery, Pageable pageable) {
        long total = countQuery.fetchOne();

        List<T> content = contentQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
