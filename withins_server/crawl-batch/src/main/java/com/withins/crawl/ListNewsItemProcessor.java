package com.withins.crawl;

import com.withins.core.exception.EntityNotFoundException;
import com.withins.core.news.component.NewsReader;
import com.withins.core.news.entity.News;
import com.withins.core.news.enums.NewsType;
import com.withins.core.welfarecenter.component.WelfareCenterReader;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class ListNewsItemProcessor implements ItemProcessor<List<S3NewsFileDto>, List<News>> {
    private final NewsReader newsReader;
    private final WelfareCenterReader welfareCenterReader;

    /*
    하나의 S3파일에 한 복지관만 크롤링한 케이스의 로직
    */
    @Override
    public List<News> process(List<S3NewsFileDto> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }

        List<News> newsList = new ArrayList<>();
        Set<String> existingLinks = newsReader.findExistingLinks(mapToLinks(items));
        WelfareCenter welfareCenter = findWelfareCenter(items);


        for (S3NewsFileDto item : items) {
            if (shouldSkipItem(item, existingLinks)) {
                continue;
            }

            newsList.add(createNewsFromItem(item, welfareCenter));
        }

        return newsList;
    }

    private News createNewsFromItem(S3NewsFileDto item, WelfareCenter welfareCenter) {
        return News.builder()
                .title(item.getTitle())
                .type(NewsType.valueOf(item.getType()))
                .link(item.getLink())
                .welfareCenter(welfareCenter)
                .build();
    }

    private boolean shouldSkipItem(S3NewsFileDto item, Set<String> existingLinks) {
        if (item.getLink() != null && existingLinks.contains(item.getLink())) {
            log.info("DB에 이미 존재하는 News 엔티티가 크롤링 데이터에 존재 - welfareCenterName={}, title={}, link={}",
                    item.getWelfareCenterName(), item.getTitle(), item.getLink());
            return true;
        }
        return false;
    }

    private WelfareCenter findWelfareCenter(final List<S3NewsFileDto> items) {
        return welfareCenterReader.readBy(items.get(0).getWelfareCenterName())
                .orElseGet(
                        () -> {
                            log.warn("크롤링한 데이터의 WelfareCenterName과 DB에 저장된 WelfareCenter의 name이 불일치 - 크롤링한 데이터의 name={}",
                                    items.get(0).getWelfareCenterName());

                            throw new EntityNotFoundException("WelfareCenter", items.get(0).getWelfareCenterName());
                        }
                );
    }

    private List<String> mapToLinks(List<S3NewsFileDto> s3NewsFileDtos) {
        return s3NewsFileDtos.stream()
                .map(S3NewsFileDto::getLink)
                .toList();
    }
}
