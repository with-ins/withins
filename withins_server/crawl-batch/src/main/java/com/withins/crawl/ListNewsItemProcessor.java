package com.withins.crawl;

import com.withins.core.welfarecenter.entity.News;
import com.withins.core.welfarecenter.entity.NewsType;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import com.withins.core.welfarecenter.repository.WelfareCenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ListNewsItemProcessor implements ItemProcessor<List<NewsDto>, List<News>> {
    private final WelfareCenterRepository welfareCenterRepository;

    @Override
    public List<News> process(List<NewsDto> items) throws Exception {
        List<News> newsList = new ArrayList<>();

        for (NewsDto item : items) {
            try {
                WelfareCenter welfareCenter = welfareCenterRepository.findByName(item.getWelfareCenterName())
                        .orElseThrow(() -> new IllegalArgumentException("WelfareCenter not found: " + item.getWelfareCenterName()));

                // Convert string type to NewsType enum
                NewsType newsType = mapToNewsType(item.getType());

                News news = News.builder()
                        .title(item.getTitle())
                        .link(item.getLink())
                        .type(newsType)
                        .welfareCenter(welfareCenter)
                        .build();

                newsList.add(news);
            } catch (Exception e) {
                log.error("Error processing item: {}", item, e);
                // Skip this item and continue with the next one
            }
        }

        return newsList;
    }

    private NewsType mapToNewsType(String type) {
        if (type == null || type.isEmpty()) {
            return NewsType.NOTICE; // Default to NOTICE if type is not specified
        }

        // Map the type string to NewsType enum
        switch (type.toUpperCase()) {
            case "NOTICE":
                return NewsType.NOTICE;
            case "WELFARE":
                return NewsType.WELFARE;
            case "RECRUIT":
            case "JOB":
                return NewsType.RECRUIT;
            case "EVENT":
            case "PROGRAM":
                return NewsType.EVENT;
            default:
                return NewsType.ALL;
        }
    }
}