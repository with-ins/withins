package com.withins.core.news.dto;

import com.withins.core.news.entity.News;
import com.withins.core.news.enums.NewsType;
import com.withins.core.welfarecenter.dto.WelfareCenterResponse;

import java.time.LocalDate;

public record NewsResponse(Long newsId,
                           String title,
                           NewsType type,
                           String link,
                           WelfareCenterResponse welfareCenter,
                           LocalDate createdAt) {

    public static NewsResponse of(News news) {
        return new NewsResponse(
            news.getId(),
            news.getTitle(),
            news.getType(),
            news.getLink(),
            WelfareCenterResponse.of(news.getWelfareCenter()),
            news.getCreatedAt()
        );
    }
}
