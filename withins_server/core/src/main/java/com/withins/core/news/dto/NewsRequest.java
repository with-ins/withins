package com.withins.core.news.dto;

import com.withins.core.news.entity.News;
import com.withins.core.news.enums.NewsType;
import com.withins.core.welfarecenter.dto.WelfareCenterRequest;

import java.time.LocalDate;

public record NewsRequest(Long newsId,
                          String title,
                          NewsType type,
                          String link,
                          WelfareCenterRequest welfareCenter,
                          LocalDate createdAt) {

    public static NewsRequest of(News news) {
        return new NewsRequest(news.getId(), news.getTitle(), news.getType(), news.getLink(), WelfareCenterRequest.of(news.getWelfareCenter()), news.getCreatedAt());
    }
}
