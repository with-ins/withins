package com.withins.api.controller;

import com.withins.core.news.dto.NewsCondition;
import com.withins.core.news.dto.NewsResponse;
import com.withins.core.news.service.NewsService;
import com.withins.core.paging.PageParams;
import com.withins.core.paging.PageWith;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<PageWith<NewsResponse>> getNews(@ModelAttribute PageParams pageParams,
                                                          @ModelAttribute NewsCondition condition) {
        var newsPageWith = newsService.search(pageParams, condition);
        return ResponseEntity.ok(newsPageWith);
    }
}
