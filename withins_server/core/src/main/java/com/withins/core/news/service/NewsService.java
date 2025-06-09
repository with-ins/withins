package com.withins.core.news.service;

import com.withins.core.news.component.NewsReader;
import com.withins.core.news.dto.NewsCondition;
import com.withins.core.news.dto.NewsResponse;
import com.withins.core.paging.PageParams;
import com.withins.core.paging.PageWith;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {

    private final NewsReader newsReader;

    public PageWith<NewsResponse> search(PageParams pageParams, NewsCondition condition) {
        Pageable pageable = PageRequest.of(pageParams.page(), pageParams.size());
        Page<NewsResponse> newsRequest = newsReader.search(pageable, condition);
        return PageWith.of(condition, newsRequest);
    }
}
