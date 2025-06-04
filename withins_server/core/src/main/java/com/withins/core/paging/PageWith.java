package com.withins.core.paging;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@ToString
public class PageWith<T> {

    private final PageResponse page;
    private final Condition condition;
    private final List<T> content;

    private PageWith(Condition condition, Page<T> page) {
        this.page = PageResponse.of(page.getTotalElements(), page.getTotalPages(), page.getNumber());
        this.condition = condition;
        this.content = page.getContent();
    }

    public static <T> PageWith<T> of(Condition condition, Page<T> page) {
        return new PageWith<>(condition, page);
    }
}
