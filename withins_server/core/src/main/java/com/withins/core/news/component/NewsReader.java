package com.withins.core.news.component;

import com.withins.core.news.dto.NewsCondition;
import com.withins.core.news.dto.NewsResponse;
import com.withins.core.news.repository.NewsQDSLRepository;
import com.withins.core.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsReader {

    private final NewsRepository newsRepository;
    private final NewsQDSLRepository newsQDSLRepository;

    public Page<NewsResponse> search(Pageable pageable, NewsCondition condition) {
        log.debug("전체 게시물 목록 조회 실행 - page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<NewsResponse> newsPage = newsQDSLRepository.readAllNewsWithPage(pageable, condition).map(NewsResponse::of);

        log.debug("전체 게시물 목록 조회 완료 - totalPages: {}, totalElements: {}, page: {}, size: {}",
                newsPage.getTotalPages(), newsPage.getTotalElements(), newsPage.getNumber(), newsPage.getSize());

        return newsPage;
    }

    public Set<String> findExistingLinks(List<String> links) {
        if(CollectionUtils.isEmpty(links)) {
            return Collections.emptySet();
        }
        return newsRepository.findLinkSetByLinkIn(links);
    }
}
