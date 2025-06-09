package com.withins.core.news.component;

import com.withins.core.news.dto.NewsCondition;
import com.withins.core.news.dto.NewsResponse;
import com.withins.core.news.repository.NewsQDSLRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsReader {

    private final NewsQDSLRepository newsQDSLRepository;

    public Page<NewsResponse> search(Pageable pageable, NewsCondition condition) {
        log.debug("전체 게시물 목록 조회 실행 - page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<NewsResponse> newsPage = newsQDSLRepository.readAllNewsWithPage(pageable, condition).map(NewsResponse::of);

        log.debug("전체 게시물 목록 조회 완료 - totalPages: {}, totalElements: {}, page: {}, size: {}",
                newsPage.getTotalPages(), newsPage.getTotalElements(), newsPage.getNumber(), newsPage.getSize());

        return newsPage;
    }
}
